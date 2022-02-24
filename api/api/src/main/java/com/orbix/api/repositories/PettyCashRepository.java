/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PettyCash;

/**
 * @author GODFREY
 *
 */
public interface PettyCashRepository extends JpaRepository<PettyCash, Long> {

}
