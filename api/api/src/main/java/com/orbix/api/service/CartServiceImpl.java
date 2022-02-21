/**
 * 
 */
package com.orbix.api.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Cart;
import com.orbix.api.domain.CartDetail;
import com.orbix.api.domain.Lpo;
import com.orbix.api.domain.Till;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.AllocationRepository;
import com.orbix.api.repositories.CartDetailRepository;
import com.orbix.api.repositories.CartRepository;
import com.orbix.api.repositories.CustomerRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.SalesInvoiceRepository;
import com.orbix.api.repositories.TillRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GODFREY
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartServiceImpl implements CartService {
	
	private final CartRepository cartRepository;
	private final CartDetailRepository cartDetailRepository;
	
	@Override
	public Cart createCart(Till till) {
		Optional<Cart> c = cartRepository.findByTill(till);
		if(c.isPresent()) {
			throw new InvalidOperationException("Could not create a new work space. An active work space already exists");
		}
		Cart newCart = new Cart();
		newCart.setNo("NA");
		newCart.setTill(till);
		newCart.setActive(true);
		newCart = cartRepository.save(newCart);
		if(newCart.getNo().equals("NA")) {
			newCart.setNo(generateCartNo(newCart));
			newCart = cartRepository.save(newCart);
		}	
		return newCart;
	}
	@Override
	public Cart loadCart(Till till) {
		Optional<Cart> c = cartRepository.findByTillAndActive(till, true);
		if(!c.isPresent()) {
			throw new NotFoundException("No active work space exist");
		}
		return c.get();
	}
	
	
	
	private String generateCartNo(Cart cart) {
		Long number = cart.getId();		
		String sNumber = number.toString();
		return "CART-"+Formater.formatNine(sNumber);
	}
	@Override
	public boolean deactivateCart(Cart cart) {
		cart.setActive(false);
		cartRepository.save(cart);
		return true;
	}
	@Override
	public Cart activateCart(Cart cart, Till till) {
		Optional<Cart> c = cartRepository.findByTillAndActive(till, true);
		c.get().setActive(false);
		cartRepository.saveAndFlush(cart);	
		Optional<Cart> c2 = cartRepository.findByTillAndActive(till, true);
		c2.get().setActive(true);
		return cartRepository.saveAndFlush(c2.get());
	}
	@Override
	public boolean addCartDetail(CartDetail cartDetail) {
		Optional<Cart> c = cartRepository.findByNo(cartDetail.getCart().getNo());
		if(!c.isPresent()) {
			throw new NotFoundException("Could not find work space");
		}
		Optional<CartDetail> d = cartDetailRepository.findByCodeAndCartAndVoided(cartDetail.getCode(), c.get(), true);
		for(int i =0; i<300; i++) {
				System.out.println(cartDetail.getSellingPriceVatIncl());
			}
		if(d.isPresent()) {			
			d.get().setQty(d.get().getQty() + cartDetail.getQty());
			cartDetailRepository.saveAndFlush(d.get());
			return true;
		}else {			
			CartDetail detail = new CartDetail();
			detail.setCart(c.get());
			detail.setBarcode(cartDetail.getBarcode());
			detail.setCode(cartDetail.getCode());
			detail.setDescription(cartDetail.getDescription());
			detail.setCostPriceVatExcl(cartDetail.getCostPriceVatExcl());
			detail.setCostPriceVatIncl(cartDetail.getCostPriceVatIncl());
			detail.setSellingPriceVatExcl(cartDetail.getSellingPriceVatExcl());
			detail.setSellingPriceVatIncl(cartDetail.getSellingPriceVatIncl());
			detail.setQty(cartDetail.getQty());
			detail.setDiscount(cartDetail.getDiscount());
			detail.setVat(cartDetail.getVat());
			detail.setVoided(false);
			cartDetailRepository.saveAndFlush(detail);
			return true;
		}
	}
	@Override
	public boolean updateQty(CartDetail cartDetail) {
		Optional<CartDetail> d = cartDetailRepository.findById(cartDetail.getId());
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}
		if(cartDetail.getQty() <= 0) {
			throw new InvalidEntryException("Invalid entry");
		}
		d.get().setQty(cartDetail.getQty());
		cartDetailRepository.saveAndFlush(d.get());
		return true;
	}
}
