/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.domain.Customer;
import com.orbix.api.service.CustomerService;
import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerResource {

	private final 	CustomerService customerService;
	
	@GetMapping("/customers")
	@PreAuthorize("hasAnyAuthority('CUSTOMER-READ')")
	public ResponseEntity<List<Customer>>getCustomers(){
		return ResponseEntity.ok().body(customerService.getAll());
	}
	
	@GetMapping("/customers/get")
	@PreAuthorize("hasAnyAuthority('CUSTOMER-READ')")
	public ResponseEntity<Customer> getCustomer(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(customerService.get(id));
	}
	
	@GetMapping("/customers/get_by_name")
	@PreAuthorize("hasAnyAuthority('CUSTOMER-READ')")
	public ResponseEntity<Customer> getCustomerByName(
			@RequestParam(name = "name") String name){
		return ResponseEntity.ok().body(customerService.getByName(name));
	}
	
	@PostMapping("/customers/create")
	@PreAuthorize("hasAnyAuthority('CUSTOMER-CREATE')")
	public ResponseEntity<Customer>createCustomer(
			@RequestBody Customer customer){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/customers/create").toUriString());
		return ResponseEntity.created(uri).body(customerService.save(customer));
	}
		
	@PutMapping("/customers/update")
	@PreAuthorize("hasAnyAuthority('CUSTOMER-UPDATE')")
	public ResponseEntity<Customer>updateCustomer(
			@RequestBody Customer customer, 
			HttpServletRequest request){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/customers/update").toUriString());
		return ResponseEntity.created(uri).body(customerService.save(customer));
	}
	
	@DeleteMapping("/customers/delete")
	@PreAuthorize("hasAnyAuthority('CUSTOMER-DELETE')")
	public ResponseEntity<Boolean> deleteCustomer(
			@RequestParam(name = "id") Long id){
		Customer customer = customerService.get(id);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/customers/delete").toUriString());
		return ResponseEntity.created(uri).body(customerService.delete(customer));
	}

}
