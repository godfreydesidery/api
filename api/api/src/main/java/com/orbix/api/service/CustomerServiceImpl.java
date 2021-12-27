/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Customer;
import com.orbix.api.domain.Till;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.CustomerRepository;
import com.orbix.api.repositories.TillRepository;

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
		return customerRepository.save(customer);
	}

	@Override
	public Customer get(Long id) {
		return customerRepository.findById(id).get();
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

}
