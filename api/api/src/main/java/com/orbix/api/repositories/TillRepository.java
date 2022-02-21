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
	 * @param no
	 * @return
	 */
	Optional<Till> findByNo(String tillNo);

	/**
	 * @param computerName
	 * @return
	 */
	Optional<Till> findByComputerName(String computerName);
}
