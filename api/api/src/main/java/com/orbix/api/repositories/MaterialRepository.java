/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Material;

/**
 * @author GODFREY
 *
 */
public interface MaterialRepository extends JpaRepository<Material, Long> {

	/**
	 * @param code
	 * @return
	 */
	Optional<Material> findByCode(String code);

	/**
	 * @param description
	 * @return
	 */
	Optional<Material> findByDescription(String description);
	
	@Query("SELECT m.description FROM Material m WHERE m.active =1")
	List<String> getActiveDescriptions();
}
