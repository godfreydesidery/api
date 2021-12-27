/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

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

}
