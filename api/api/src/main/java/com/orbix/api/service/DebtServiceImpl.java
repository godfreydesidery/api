/**
 * 
 */
package com.orbix.api.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Employee;
import com.orbix.api.domain.PackingList;
import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Debt;
import com.orbix.api.domain.Debt;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.models.DebtModel;
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
public class DebtServiceImpl implements DebtService {
	
	private final DebtRepository debtRepository;

	@Override
	public Debt create(Debt debt) {
		/**
		 * First register debt payment, to be implemented later
		 * then pay debt
		 */
		Debt d = debtRepository.saveAndFlush(debt);
		if(d.getNo().equals("NA")) {
			d.setNo(generateDebtNo(d));
		}
		return debtRepository.saveAndFlush(d);
		
	}

	@Override
	public Debt pay(Debt debt, double amount) {
		/**
		 * First register debt payment, to be implemented later
		 * then pay debt
		 */
		if(amount <= 0) {
			throw new InvalidEntryException("Invalid amount");
		}else if(amount > debt.getAmount()) {
			throw new InvalidEntryException("Payment amount exceeds debt amount");
		}
		debt.setAmount(debt.getAmount() - amount);
		return debtRepository.saveAndFlush(debt);
	}
	
	@Override
	public List<DebtModel> getByEmployeeAndApprovedOrPartial(Employee employee) {
		List<String> statuses = new ArrayList<String>();
		statuses.add("PENDING");
		statuses.add("PARTIAL");
		List<Debt> debts = debtRepository.findByEmployeeAndPendingOrPartial(employee, statuses);
		List<DebtModel> models = new ArrayList<DebtModel>();
		for(Debt d : debts) {
			DebtModel model = new DebtModel();
			model.setId(d.getId());
			model.setNo(d.getNo());
			model.setEmployee(d.getEmployee());
			model.setStatus(d.getStatus());
			model.setBalance(d.getBalance());
//			if(inv.getCreatedAt() != null && inv.getCreatedBy() != null) {
//				model.setCreated(dayRepository.findById(inv.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(inv.getCreatedBy()));
//			}
//			if(inv.getApprovedAt() != null && inv.getApprovedBy() != null) {
//				model.setApproved(dayRepository.findById(inv.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(inv.getApprovedBy()));
//			}			
			models.add(model);
		}
		return models;
	}
	
	private String generateDebtNo(Debt debt) {
		Long number = debt.getId();		
		String sNumber = number.toString();
		return "DBT-"+Formater.formatNine(sNumber);
	}

}
