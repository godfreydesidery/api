/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Customer;

/**
 * @author GODFREY
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<Customer> findByName(String name);

	/**
	 * @param no
	 * @return
	 */
	Optional<Customer> findByNo(String no);
	
	@Query("SELECT c.name FROM Customer c WHERE c.active =1")
	List<String> getActiveNames();

}
