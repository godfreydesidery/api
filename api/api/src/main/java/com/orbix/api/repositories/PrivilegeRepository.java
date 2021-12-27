/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Privilege;
import com.orbix.api.domain.Role;

/**
 * @author GODFREY
 *
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
	Optional<Privilege> findByName(String name);
	
	boolean existsByName(String name);
}
