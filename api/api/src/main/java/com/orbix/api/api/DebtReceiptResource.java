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

import com.orbix.api.domain.Employee;
import com.orbix.api.domain.DebtReceipt;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.DebtReceiptModel;
import com.orbix.api.repositories.EmployeeRepository;
import com.orbix.api.repositories.DebtReceiptRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.DebtReceiptService;
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
public class DebtReceiptResource {
	private final 	UserService userService;
	private final 	DayService dayService;
	private final 	DebtReceiptService debtReceiptService;
	private final 	DebtReceiptRepository debtReceiptRepository;
	private final 	EmployeeRepository customerRepository;
	
	
	@GetMapping("/debt_receipts")
	@PreAuthorize("hasAnyAuthority('DEBT_RECEIPT-READ')")
	public ResponseEntity<List<DebtReceiptModel>>getDebtReceipts(){
		return ResponseEntity.ok().body(debtReceiptService.getAllVisible());
	}
	
	@GetMapping("/debt_receipts/get")
	@PreAuthorize("hasAnyAuthority('DEBT_RECEIPT-READ')")
	public ResponseEntity<DebtReceiptModel> getDebtReceipt(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(debtReceiptService.get(id));
	}
	
	@GetMapping("/debt_receipts/get_by_no")
	@PreAuthorize("hasAnyAuthority('DEBT_RECEIPT-READ')")
	public ResponseEntity<DebtReceiptModel> getDebtReceiptByNo(
			@RequestParam(name = "no") String no){
		return ResponseEntity.ok().body(debtReceiptService.getByNo(no));
	}
	
	@PostMapping("/debt_receipts/create")
	@PreAuthorize("hasAnyAuthority('DEBT_RECEIPT-CREATE')")
	public ResponseEntity<DebtReceiptModel>createDebtReceipt(
			@RequestBody DebtReceipt debtReceipt,
			HttpServletRequest request){
		Optional<Employee> c = customerRepository.findByNo(debtReceipt.getEmployee().getNo());
		if(!c.isPresent()) {
			throw new NotFoundException("Employee not found");
		}
		DebtReceipt rec = new DebtReceipt();
		rec.setNo("NA");
		rec.setEmployee(c.get());
		rec.setStatus("PENDING");
		rec.setReceiptDate(debtReceipt.getReceiptDate());
		rec.setMode(debtReceipt.getMode());
		rec.setAmount(debtReceipt.getAmount());
		rec.setChequeNo(debtReceipt.getChequeNo());
		rec.setComments(debtReceipt.getComments());	
		rec.setCreatedBy(userService.getUserId(request));
		rec.setCreatedAt(dayService.getDayId());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/debt_receipts/create").toUriString());
		return ResponseEntity.created(uri).body(debtReceiptService.save(rec));
	}
	
	@PutMapping("/debt_receipts/update")
	@PreAuthorize("hasAnyAuthority('DEBT_RECEIPT-UPDATE')")
	public ResponseEntity<DebtReceiptModel>updateDebtReceipt(
			@RequestBody DebtReceipt debtReceipt,
			HttpServletRequest request){
		Optional<Employee> c = customerRepository.findByNo(debtReceipt.getEmployee().getNo());
		if(!c.isPresent()) {
			throw new NotFoundException("Employee not found");
		}
		Optional<DebtReceipt> l = debtReceiptRepository.findById(debtReceipt.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("DEBT_RECEIPT not found");
		}
		if(!l.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only Pending Debt Receipts can be edited");
		}
		
		if(l.get().getEmployee().equals(c.get()) && !l.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Changing Employee is not allowed for a non pending receipt");
		}		
		l.get().setEmployee(c.get());
		l.get().setReceiptDate(debtReceipt.getReceiptDate());
		l.get().setMode(debtReceipt.getMode());
		l.get().setChequeNo(debtReceipt.getChequeNo());
		l.get().setAmount(debtReceipt.getAmount());
		l.get().setComments(debtReceipt.getComments());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/debt_receipts/update").toUriString());
		return ResponseEntity.created(uri).body(debtReceiptService.save(l.get()));
	}
	
	@PutMapping("/debt_receipts/approve")
	@PreAuthorize("hasAnyAuthority('DEBT_RECEIPT-APPROVE')")
	public ResponseEntity<DebtReceiptModel>approveDebtReceipt(
			@RequestBody DebtReceipt debtReceipt,
			HttpServletRequest request){		
		Optional<DebtReceipt> l = debtReceiptRepository.findById(debtReceipt.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("DEBT_RECEIPT not found");
		}
		if(l.get().getStatus().equals("PENDING")) {
			l.get().setApprovedBy(userService.getUserId(request));
			l.get().setApprovedAt(dayService.getDayId());
			l.get().setStatus("APPROVED");
		}else {
			throw new InvalidOperationException("Could not approve, not a PENDING DEBT_RECEIPT");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/debt_receipts/approve").toUriString());
		return ResponseEntity.created(uri).body(debtReceiptService.approve(l.get()));
	}
	
	@PutMapping("/debt_receipts/cancel")
	@PreAuthorize("hasAnyAuthority('DEBT_RECEIPT-CANCEL')")
	public ResponseEntity<DebtReceiptModel>cancelDebtReceipt(
			@RequestBody DebtReceipt debtReceipt,
			HttpServletRequest request){		
		Optional<DebtReceipt> l = debtReceiptRepository.findById(debtReceipt.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("DEBT_RECEIPT not found");
		}
		if(l.get().getStatus().equals("PENDING")) {
			l.get().setStatus("CANCELED");
		}else {
			throw new InvalidOperationException("Could not cancel, only Pending DEBT_RECEIPTs can be canceled");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/debt_receipts/cancel").toUriString());
		return ResponseEntity.created(uri).body(debtReceiptService.save(l.get()));
	}
	
	@PutMapping("/debt_receipts/archive")
	@PreAuthorize("hasAnyAuthority('DEBT_RECEIPT-CREATE','DEBT_RECEIPT-UPDATE','DEBT_RECEIPT-ARCHIVE')")
	public ResponseEntity<Boolean>archiveDebtReceipt(
			@RequestBody DebtReceipt debtReceipt,
			HttpServletRequest request){		
		Optional<DebtReceipt> l = debtReceiptRepository.findById(debtReceipt.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("DEBT_RECEIPT not found");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/debt_receipts/archive").toUriString());
		return ResponseEntity.created(uri).body(debtReceiptService.archive(l.get()));
	}
	
	@PutMapping("/debt_receipts/archive_all")
	@PreAuthorize("hasAnyAuthority('DEBT_RECEIPT-CREATE','DEBT_RECEIPT-UPDATE','DEBT_RECEIPT-ARCHIVE')")
	public ResponseEntity<Boolean>archiveDebtReceipts(){			
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/debt_receipts/archive_all").toUriString());
		return ResponseEntity.created(uri).body(debtReceiptService.archiveAll());
	}
}
