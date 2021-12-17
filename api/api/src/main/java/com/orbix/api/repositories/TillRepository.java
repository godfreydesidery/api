/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Till;
import java.util.Optional;

/**
 * @author GODFREY
 *
 */
public interface TillRepository extends JpaRepository<Till, Long> {

	/**
	 * @param tillNo
	 * @return
	 */
	Optional<Till> findByTillNo(String tillNo);
}
