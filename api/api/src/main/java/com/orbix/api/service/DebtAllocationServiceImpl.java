/**
 * 
 */
package com.orbix.api.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.DebtAllocation;
import com.orbix.api.domain.Employee;
import com.orbix.api.domain.PackingList;
import com.orbix.api.domain.Debt;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.DebtAllocationRepository;
import com.orbix.api.repositories.EmployeeRepository;
import com.orbix.api.repositories.PackingListRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.DebtRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GODFREY
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DebtAllocationServiceImpl implements DebtAllocationService {
	private final DebtAllocationRepository debtAllocationRepository;
	private final EmployeeRepository employeeRepository;
	private final DebtRepository debtRepository;
	private final UserService userService;
	private final DayService dayService;
	private final DayRepository dayRepository;
	private final PackingListRepository packingListRepository;

	@Override
	public boolean allocate(Employee employee, Debt debt, HttpServletRequest request) {
		Optional<Employee> e = employeeRepository.findById(employee.getId());
		if(!e.isPresent()) {
			throw new NotFoundException("Employee not found in database");
		}
		Optional<Debt> d = debtRepository.findById(debt.getId());
		if(!d.isPresent()) {
			throw new NotFoundException("Debt not found in database");
		}
		if(d.get().getPackingList() != null) {
			if(e.get().getId() != d.get().getPackingList().getEmployee().getId()) {
				throw new InvalidOperationException("Employee and invoice do not match");
			}
		}else {
			throw new InvalidOperationException("Debt has no reference");
		}
		
		double employeeBalance = employee.getBalance();
		if(employeeBalance <= 0) {
			throw new InvalidOperationException("Could not process, no balance available");
		}
		double referenceBalance = 0;
		double debtAllocationAmount = 0;
		if(d.get().getPackingList() != null) {
			referenceBalance = d.get().getPackingList().getTotalDeficit();
			if(referenceBalance <= 0) {
				throw new InvalidOperationException("Could not process, reference document has no deficit");
			}
			Optional<PackingList> p = packingListRepository.findById(d.get().getPackingList().getId());
			
			if(employeeBalance >= debt.getBalance()) {
				double balance = debt.getBalance();
				p.get().setTotalOther(p.get().getTotalOther() + balance);
				p.get().setTotalDeficit(p.get().getTotalDeficit() - balance);
				packingListRepository.saveAndFlush(p.get());
				double newEmployeeBalance = employeeBalance - debt.getBalance();
				balance = 0;
				debtAllocationAmount = debt.getBalance();
				employee.setBalance(newEmployeeBalance);
				debt.setBalance(balance);
				debt.setStatus("PAID");
				employeeRepository.saveAndFlush(employee);
				debtRepository.saveAndFlush(debt);				
			}else if(employeeBalance < debt.getBalance()) {
				p.get().setTotalOther(p.get().getTotalOther() + employeeBalance);
				p.get().setTotalDeficit(p.get().getTotalDeficit() - employeeBalance);
				packingListRepository.saveAndFlush(p.get());
				double newEmployeeBalance = 0;
				double balance = debt.getBalance() - employeeBalance;
				debtAllocationAmount = employeeBalance;
				employee.setBalance(newEmployeeBalance);
				debt.setBalance(balance);
				debt.setStatus("PARTIAL");
				employeeRepository.saveAndFlush(employee);
				debtRepository.saveAndFlush(debt);
			}			
		}
		
		DebtAllocation debtAllocation = new DebtAllocation();
		debtAllocation.setNo("NA");
		debtAllocation.setDebt(debt);
		debtAllocation.setAmount(debtAllocationAmount);
		debtAllocation.setCreatedBy(userService.getUserId(request));
		debtAllocation.setCreatedAt(dayRepository.getCurrentBussinessDay().getId());
		DebtAllocation a = debtAllocationRepository.saveAndFlush(debtAllocation);
		if(a.getNo().equals("NA")) {
			a.setNo(generateDebtAllocationNo(a));
			a = debtAllocationRepository.saveAndFlush(a);
		}
		return true;		
	}
	
	private String generateDebtAllocationNo(DebtAllocation debtAllocation) {
		Long number = debtAllocation.getId();		
		String sNumber = number.toString();
		return "DAC-"+Formater.formatSix(sNumber);
	}
}
