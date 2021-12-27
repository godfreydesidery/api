/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.User;

/**
 * @author GODFREY
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
	@Query("SELECT u.alias FROM User u WHERE u.id =:id")
	String getAlias(Long id);
}
