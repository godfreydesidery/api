/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.SubClass;

/**
 * @author GODFREY
 *
 */
public interface SubClassRepository extends JpaRepository<SubClass, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<SubClass> findByName(String name);

}
