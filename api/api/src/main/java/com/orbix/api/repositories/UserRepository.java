/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.User;

/**
 * @author GODFREY
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
