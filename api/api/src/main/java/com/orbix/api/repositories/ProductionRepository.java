/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Production;

/**
 * @author GODFREY
 *
 */
public interface ProductionRepository extends JpaRepository<Production, Long> {

	/**
	 * @param no
	 * @return
	 */
	Optional<Production> findByNo(String no);

}
