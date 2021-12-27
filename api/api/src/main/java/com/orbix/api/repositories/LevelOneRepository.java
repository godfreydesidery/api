/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.LevelOne;

/**
 * @author GODFREY
 *
 */
public interface LevelOneRepository extends JpaRepository<LevelOne, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<LevelOne> findByName(String name);

}
