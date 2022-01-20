/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Customer;

/**
 * @author GODFREY
 *
 */
public interface CustomerService {
	Customer save(Customer customer);
	Customer get(Long id);
	Customer getByNo(String no);
	Customer getByName(String name);
	boolean delete(Customer customer);
	List<Customer>getAll(); //edit this to limit the number, for perfomance.
	List<String> getNames();
}
