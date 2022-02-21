/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Cart;
import com.orbix.api.domain.CartDetail;

/**
 * @author GODFREY
 *
 */
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {	
	/**
	 * @param code
	 * @param cart
	 * @param b
	 * @return
	 */
	Optional<CartDetail> findByCodeAndCartAndVoided(String code, Cart cart, boolean b);

}
