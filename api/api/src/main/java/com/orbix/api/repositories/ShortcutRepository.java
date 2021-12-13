/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Shortcut;
import com.orbix.api.domain.User;

/**
 * @author GODFREY
 *
 */
public interface ShortcutRepository extends JpaRepository<Shortcut, Long> {

	/**
	 * @param link
	 * @param user
	 * @return
	 */
	Optional<User> findByLinkAndUser(String link, User user);

	/**
	 * @param user
	 * @return
	 */
	List<Shortcut> findByUser(User user);

	

}
