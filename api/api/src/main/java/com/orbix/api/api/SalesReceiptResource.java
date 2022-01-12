/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.domain.Customer;
import com.orbix.api.domain.SalesReceipt;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.SalesReceiptModel;
import com.orbix.api.repositories.CustomerRepository;
import com.orbix.api.repositories.SalesReceiptRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.SalesReceiptService;
import com.orbix.api.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class SalesReceiptResource {
	
	private final 	UserService userService;
	private final 	DayService dayService;
	private final 	SalesReceiptService salesReceiptService;
	private final 	SalesReceiptRepository salesReceiptRepository;
	private final 	CustomerRepository customerRepository;
	
	
	@GetMapping("/sales_receipts")
	@PreAuthorize("hasAnyAuthority('SALES_RECEIPT-READ')")
	public ResponseEntity<List<SalesReceiptModel>>getSalesReceipts(){
		return ResponseEntity.ok().body(salesReceiptService.getAllVisible());
	}
	
	@GetMapping("/sales_receipts/get")
	@PreAuthorize("hasAnyAuthority('SALES_RECEIPT-READ')")
	public ResponseEntity<SalesReceiptModel> getSalesReceipt(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(salesReceiptService.get(id));
	}
	
	@GetMapping("/sales_receipts/get_by_no")
	@PreAuthorize("hasAnyAuthority('SALES_RECEIPT-READ')")
	public ResponseEntity<SalesReceiptModel> getSalesReceiptByNo(
			@RequestParam(name = "no") String no){
		return ResponseEntity.ok().body(salesReceiptService.getByNo(no));
	}
	
	@PostMapping("/sales_receipts/create")
	@PreAuthorize("hasAnyAuthority('SALES_RECEIPT-CREATE')")
	public ResponseEntity<SalesReceiptModel>createSalesReceipt(
			@RequestBody SalesReceipt salesReceipt,
			HttpServletRequest request){
		Optional<Customer> c = customerRepository.findByNo(salesReceipt.getCustomer().getNo());
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found");
		}
		SalesReceipt rec = new SalesReceipt();
		rec.setNo("NA");
		rec.setCustomer(c.get());
		rec.setStatus("PENDING");
		rec.setReceiptDate(salesReceipt.getReceiptDate());
		rec.setMode(salesReceipt.getMode());
		rec.setAmount(salesReceipt.getAmount());
		rec.setChequeNo(salesReceipt.getChequeNo());
		rec.setComments(salesReceipt.getComments());	
		rec.setCreatedBy(userService.getUserId(request));
		rec.setCreatedAt(dayService.getDayId());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_receipts/create").toUriString());
		return ResponseEntity.created(uri).body(salesReceiptService.save(rec));
	}
	
	@PutMapping("/sales_receipts/update")
	@PreAuthorize("hasAnyAuthority('SALES_RECEIPT-UPDATE')")
	public ResponseEntity<SalesReceiptModel>updateSalesReceipt(
			@RequestBody SalesReceipt salesReceipt,
			HttpServletRequest request){
		Optional<Customer> c = customerRepository.findByNo(salesReceipt.getCustomer().getNo());
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found");
		}
		Optional<SalesReceipt> l = salesReceiptRepository.findById(salesReceipt.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("SALES_RECEIPT not found");
		}
		if(!l.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only Pending Sales Invoices can be edited");
		}
		
		if(l.get().getCustomer().equals(c.get()) && !l.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Changing Customer is not allowed for a non pending receipt");
		}		
		l.get().setCustomer(c.get());
		l.get().setReceiptDate(salesReceipt.getReceiptDate());
		l.get().setMode(salesReceipt.getMode());
		l.get().setChequeNo(salesReceipt.getChequeNo());
		l.get().setAmount(salesReceipt.getAmount());
		l.get().setComments(salesReceipt.getComments());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_receipts/update").toUriString());
		return ResponseEntity.created(uri).body(salesReceiptService.save(l.get()));
	}
	
	@PutMapping("/sales_receipts/approve")
	@PreAuthorize("hasAnyAuthority('SALES_RECEIPT-APPROVE')")
	public ResponseEntity<SalesReceiptModel>approveSalesReceipt(
			@RequestBody SalesReceipt salesReceipt,
			HttpServletRequest request){		
		Optional<SalesReceipt> l = salesReceiptRepository.findById(salesReceipt.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("SALES_RECEIPT not found");
		}
		if(l.get().getStatus().equals("PENDING")) {
			l.get().setApprovedBy(userService.getUserId(request));
			l.get().setApprovedAt(dayService.getDayId());
			l.get().setStatus("APPROVED");
		}else {
			throw new InvalidOperationException("Could not approve, not a PENDING SALES_RECEIPT");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_receipts/approve").toUriString());
		return ResponseEntity.created(uri).body(salesReceiptService.approve(l.get()));
	}
	
	@PutMapping("/sales_receipts/cancel")
	@PreAuthorize("hasAnyAuthority('SALES_RECEIPT-CANCEL')")
	public ResponseEntity<SalesReceiptModel>cancelSalesReceipt(
			@RequestBody SalesReceipt salesReceipt,
			HttpServletRequest request){		
		Optional<SalesReceipt> l = salesReceiptRepository.findById(salesReceipt.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("SALES_RECEIPT not found");
		}
		if(l.get().getStatus().equals("PENDING")) {
			l.get().setStatus("CANCELED");
		}else {
			throw new InvalidOperationException("Could not cancel, only Pending SALES_RECEIPTs can be canceled");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_receipts/cancel").toUriString());
		return ResponseEntity.created(uri).body(salesReceiptService.save(l.get()));
	}
	
	@PutMapping("/sales_receipts/archive")
	@PreAuthorize("hasAnyAuthority('SALES_RECEIPT-CREATE','SALES_RECEIPT-UPDATE','SALES_RECEIPT-ARCHIVE')")
	public ResponseEntity<Boolean>archiveSalesReceipt(
			@RequestBody SalesReceipt salesReceipt,
			HttpServletRequest request){		
		Optional<SalesReceipt> l = salesReceiptRepository.findById(salesReceipt.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("SALES_RECEIPT not found");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_receipts/archive").toUriString());
		return ResponseEntity.created(uri).body(salesReceiptService.archive(l.get()));
	}
	
	@PutMapping("/sales_receipts/archive_all")
	@PreAuthorize("hasAnyAuthority('SALES_RECEIPT-CREATE','SALES_RECEIPT-UPDATE','SALES_RECEIPT-ARCHIVE')")
	public ResponseEntity<Boolean>archiveSalesReceipts(){			
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_receipts/archive_all").toUriString());
		return ResponseEntity.created(uri).body(salesReceiptService.archiveAll());
	}
}
