/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Voided;

/**
 * @author GODFREY
 *
 */
public interface VoidedRepository extends JpaRepository<Voided, Long> {

	/**
	 * @param refId
	 * @return
	 */
	Optional<Voided> findByRefId(Long refId);

}
