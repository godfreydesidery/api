/**
 * 
 */
package com.orbix.api.service;

import com.orbix.api.domain.Cart;
import com.orbix.api.domain.CartDetail;
import com.orbix.api.domain.Till;

/**
 * @author GODFREY
 *
 */
public interface CartService {
	Cart createCart(Till till);
	Cart loadCart(Till till);
	boolean deactivateCart(Cart cart);
	Cart activateCart(Cart cart, Till till);
	boolean addCartDetail(CartDetail cartDetail);
	boolean updateQty(CartDetail cartDetail);
}
