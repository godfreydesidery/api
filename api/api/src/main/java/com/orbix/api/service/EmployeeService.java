/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Customer;
import com.orbix.api.domain.Employee;

/**
 * @author GODFREY
 *
 */
public interface EmployeeService {
	Employee save(Employee employee);
	Employee get(Long id);
	Employee getByNo(String no);
	Employee getByName(String name);
	boolean delete(Employee customer);
	List<Employee>getAll(); //edit this to limit the number, for perfomance.
	List<String> getAliases();
}
