/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

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

}
