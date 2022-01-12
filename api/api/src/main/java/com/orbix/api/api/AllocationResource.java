/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.domain.Customer;
import com.orbix.api.domain.SalesInvoice;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.CustomerRepository;
import com.orbix.api.repositories.SalesInvoiceRepository;
import com.orbix.api.service.AllocationService;
import com.orbix.api.service.CustomerService;

import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AllocationResource {
	
	private final CustomerRepository customerRepository;
	private final SalesInvoiceRepository salesInvoiceRepository;
	
	private final 	CustomerService customerService;
	private final 	AllocationService allocationService;
	
	@PostMapping("/allocations/allocate")
	@PreAuthorize("hasAnyAuthority('ALLOCATION-CREATE')")
	public ResponseEntity<Boolean>allocate(
			@RequestParam(name = "customer_id") Long customerId,
			@RequestParam(name = "sales_invoice_id") Long salesInvoiceId,
			HttpServletRequest request){
		Optional<Customer> c = customerRepository.findById(customerId);
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found in database");
		}
		Optional<SalesInvoice> i = salesInvoiceRepository.findById(salesInvoiceId);
		if(!i.isPresent()) {
			throw new NotFoundException("Invoice not found in database");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/allocations/allocate").toUriString());
		return ResponseEntity.created(uri).body(allocationService.allocate(c.get(), i.get(), request));
	}
}
