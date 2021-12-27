/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.LevelOne;
import com.orbix.api.domain.LevelThree;

/**
 * @author GODFREY
 *
 */
public interface LevelThreeRepository extends JpaRepository<LevelThree, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<LevelThree> findByName(String name);

}
