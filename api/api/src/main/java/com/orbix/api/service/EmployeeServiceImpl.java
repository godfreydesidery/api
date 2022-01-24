/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Employee;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.EmployeeRepository;

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
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;

	@Override
	public Employee save(Employee employee) {
		validateEmployee(employee);
		log.info("Saving employee to the database");
		employee.setAlias(employee.getFirstName()+" "+employee.getSecondName()+" "+employee.getLastName());
		Employee c = employeeRepository.saveAndFlush(employee);		
		if(c.getNo().equals("NA")) {
			c.setNo(generateEmployeeNo(c));
			c = employeeRepository.saveAndFlush(c);
		}
		return employeeRepository.save(c);
	}

	@Override
	public Employee get(Long id) {
		return employeeRepository.findById(id).get();
	}
	
	@Override
	public Employee getByNo(String no) {
		Optional<Employee> employee = employeeRepository.findByNo(no);
		if(!employee.isPresent()) {
			throw new NotFoundException("Employee not found");
		}
		return employee.get();
	}

	@Override
	public Employee getByName(String name) {
		Optional<Employee> employee = employeeRepository.findByAlias(name);
		if(!employee.isPresent()) {
			throw new NotFoundException("Employee not found");
		}
		return employee.get();
	}

	@Override
	public boolean delete(Employee employee) {
		if(allowDelete(employee)) {
			employeeRepository.delete(employee);
		}else {
			return false;
		}
		return true;
	}

	@Override
	public List<Employee> getAll() {
		log.info("Fetching all employees");
		return employeeRepository.findAll();
	}
	
	private boolean validateEmployee(Employee employee) {
		/**
		 * Put validation logic, throw Invalid exception if not valid
		 */
		
		
		return true;
	}
	
	private boolean allowDelete(Employee employee) {
		/**
		 * Put logic to allow till deletion, return false if not allowed, else return true
		 */
		
		throw new InvalidOperationException("Deleting the selected employee is not allowed");
		
		//return true;
	}

	@Override
	public List<String> getAliases() {
		return employeeRepository.getActiveNames();
	}
	
	private String generateEmployeeNo(Employee employee) {
		Long number = employee.getId();		
		String sNumber = number.toString();
		return "EMP-"+Formater.formatSix(sNumber);
	}
}
