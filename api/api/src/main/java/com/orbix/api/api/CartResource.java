/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.domain.Cart;
import com.orbix.api.domain.CartDetail;
import com.orbix.api.domain.Material;
import com.orbix.api.domain.Payment;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.Receipt;
import com.orbix.api.domain.Till;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.CartRepository;
import com.orbix.api.repositories.CustomerRepository;
import com.orbix.api.repositories.SalesInvoiceRepository;
import com.orbix.api.repositories.TillRepository;
import com.orbix.api.service.AllocationService;
import com.orbix.api.service.CartService;
import com.orbix.api.service.CustomerService;

import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartResource {
	private final TillRepository tillRepository;
	private final SalesInvoiceRepository salesInvoiceRepository;
	private final CartRepository cartRepository;
	private final 	CartService cartService;
	private final 	AllocationService allocationService;
	
	@GetMapping("/carts/load")
	//@PreAuthorize("hasAnyAuthority('MATERIAL-READ')")
	public ResponseEntity<Cart> loadCart(
			@RequestParam(name = "till_no") String no){
		Optional<Till> t = tillRepository.findByNo(no);
		if(!t.isPresent()) {
			throw new NotFoundException("Till not found");
		}
		return ResponseEntity.ok().body(cartService.loadCart(t.get()));
	}
	
	@GetMapping("/carts/create")
	//@PreAuthorize("hasAnyAuthority('MATERIAL-READ')")
	public ResponseEntity<Cart> createCart(
			@RequestParam(name = "till_no") String no){
		Optional<Till> t = tillRepository.findByNo(no);
		if(!t.isPresent()) {
			throw new NotFoundException("Till not found");
		}
		
		return ResponseEntity.ok().body(cartService.createCart(t.get()));
	}
	
	@PostMapping("/carts/add_detail")
	//@PreAuthorize("hasAnyAuthority('PRODUCT-CREATE')")
	public ResponseEntity<Boolean>addDetail(
			@RequestBody CartDetail cartDetail){		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/carts/add_detail").toUriString());
		return ResponseEntity.created(uri).body(cartService.addCartDetail(cartDetail));
	}
	
	@PostMapping("/carts/update_qty")
	//@PreAuthorize("hasAnyAuthority('PRODUCT-CREATE')")
	public ResponseEntity<Boolean>updateQty(
			@RequestBody CartDetail cartDetail){		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/carts/update_qty").toUriString());
		return ResponseEntity.created(uri).body(cartService.updateQty(cartDetail));
	}
	
	@PostMapping("/carts/pay")
	//@PreAuthorize("hasAnyAuthority('PRODUCT-CREATE')")
	public ResponseEntity<Receipt>pay(
			@RequestBody Payment payment,
			@RequestParam(name = "till_no") String tillNo,
			@RequestParam(name = "cart_no") String cartNo,
			HttpServletRequest request){
		Optional<Cart> c = cartRepository.findByNo(cartNo);
		if(!c.isPresent()) {
			throw new NotFoundException("Workspace not found in database");
		}		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/carts/pay").toUriString());
		return ResponseEntity.created(uri).body(cartService.pay(payment, c.get(), request));
	}
}
