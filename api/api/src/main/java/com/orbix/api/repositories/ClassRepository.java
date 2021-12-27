/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Class;

/**
 * @author GODFREY
 *
 */
public interface ClassRepository extends JpaRepository<Class, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<Class> findByName(String name);

}
