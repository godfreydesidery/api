/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Employee;
import com.orbix.api.domain.Employee;

/**
 * @author GODFREY
 *
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	/**
	 * @param name
	 * @return
	 */
	Optional<Employee> findByAlias(String name);

	/**
	 * @param no
	 * @return
	 */
	Optional<Employee> findByNo(String no);
	
	@Query("SELECT e.alias FROM Employee e WHERE e.active =1")
	List<String> getActiveNames();
}
