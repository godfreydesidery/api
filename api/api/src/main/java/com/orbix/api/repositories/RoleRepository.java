/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Role;

/**
 * @author GODFREY
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
