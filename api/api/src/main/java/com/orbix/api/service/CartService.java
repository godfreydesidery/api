/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Cart;
import com.orbix.api.domain.CartDetail;
import com.orbix.api.domain.Payment;
import com.orbix.api.domain.Receipt;
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
	boolean updateDiscount(CartDetail cartDetail);
	boolean voidd(CartDetail cartDetail, HttpServletRequest request);
	boolean unvoid(CartDetail cartDetail);
	Receipt pay(Payment payment, Cart cart, HttpServletRequest request);
}
