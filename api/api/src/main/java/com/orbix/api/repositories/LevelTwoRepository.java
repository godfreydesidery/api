/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.LevelTwo;

/**
 * @author GODFREY
 *
 */
public interface LevelTwoRepository extends JpaRepository<LevelTwo, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<LevelTwo> findByName(String name);

}
