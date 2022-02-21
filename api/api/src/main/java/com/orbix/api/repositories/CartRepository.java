/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Cart;
import com.orbix.api.domain.Till;

/**
 * @author GODFREY
 *
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

	/**
	 * @param till
	 * @return
	 */
	Optional<Cart> findByTill(Till till);

	/**
	 * @param till
	 * @param b
	 * @return
	 */
	Optional<Cart> findByTillAndActive(Till till, boolean b);

	/**
	 * @param no
	 * @return
	 */
	Optional<Cart> findByNo(String no);

}
