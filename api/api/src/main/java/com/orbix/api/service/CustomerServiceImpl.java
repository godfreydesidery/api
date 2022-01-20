/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Customer;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.CustomerRepository;
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
public class CustomerServiceImpl implements CustomerService {
	
	private final CustomerRepository customerRepository;

	@Override
	public Customer save(Customer customer) {
		validateCustomer(customer);
		log.info("Saving customer to the database");
		Customer c = customerRepository.saveAndFlush(customer);
		if(c.getNo().equals("NA")) {
			c.setNo(generateCustomerNo(c));
			c = customerRepository.saveAndFlush(c);
		}
		return customerRepository.save(c);
	}

	@Override
	public Customer get(Long id) {
		return customerRepository.findById(id).get();
	}
	
	@Override
	public Customer getByNo(String no) {
		Optional<Customer> customer = customerRepository.findByNo(no);
		if(!customer.isPresent()) {
			throw new NotFoundException("Customer not found");
		}
		return customer.get();
	}

	@Override
	public Customer getByName(String name) {
		Optional<Customer> customer = customerRepository.findByName(name);
		if(!customer.isPresent()) {
			throw new NotFoundException("Customer not found");
		}
		return customer.get();
	}

	@Override
	public boolean delete(Customer customer) {
		if(allowDelete(customer)) {
			customerRepository.delete(customer);
		}else {
			return false;
		}
		return true;
	}

	@Override
	public List<Customer> getAll() {
		log.info("Fetching all customers");
		return customerRepository.findAll();
	}
	
	private boolean validateCustomer(Customer customer) {
		/**
		 * Put validation logic, throw Invalid exception if not valid
		 */
		
		return true;
	}
	
	private boolean allowDelete(Customer customer) {
		/**
		 * Put logic to allow till deletion, return false if not allowed, else return true
		 */
		return true;
	}

	@Override
	public List<String> getNames() {
		return customerRepository.getActiveNames();
	}
	
	private String generateCustomerNo(Customer customer) {
		Long number = customer.getId();		
		String sNumber = number.toString();
		return "CUS-"+Formater.formatSix(sNumber);
	}

}
