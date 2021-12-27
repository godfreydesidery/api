/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.SubCategory;

/**
 * @author GODFREY
 *
 */
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<SubCategory> findByName(String name);

}
