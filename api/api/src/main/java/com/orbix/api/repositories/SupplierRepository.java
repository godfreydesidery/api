/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Supplier;

/**
 * @author GODFREY
 *
 */
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<Supplier> findByName(String name);

	/**
	 * @param code
	 * @return
	 */
	Optional<Supplier> findByCode(String code);
	
	@Query("SELECT s.name FROM Supplier s WHERE s.active =1")
	List<String> getActiveNames();
}
