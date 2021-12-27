/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.LevelFour;

/**
 * @author GODFREY
 *
 */
public interface LevelFourRepository extends JpaRepository<LevelFour, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<LevelFour> findByName(String name);

}
