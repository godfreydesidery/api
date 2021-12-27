/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Category;

/**
 * @author GODFREY
 *
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<Category> findByName(String name);

}
