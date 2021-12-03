/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Privilege;

/**
 * @author GODFREY
 *
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
	Privilege findByName(String name);
}
