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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.domain.Employee;
import com.orbix.api.domain.SalesInvoice;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.EmployeeRepository;
import com.orbix.api.repositories.SalesInvoiceRepository;
import com.orbix.api.service.DebtAllocationService;
import com.orbix.api.service.EmployeeService;

import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DebtAllocationResource {
	private final EmployeeRepository employeeRepository;
	private final SalesInvoiceRepository salesInvoiceRepository;
	
	private final 	EmployeeService employeeService;
	private final 	DebtAllocationService debtAllocationService;
	
	@PostMapping("/debtAllocations/allocate")
	@PreAuthorize("hasAnyAuthority('ALLOCATION-CREATE')")
	public ResponseEntity<Boolean>allocate(
			@RequestParam(name = "employee_id") Long employeeId,
			@RequestParam(name = "sales_invoice_id") Long salesInvoiceId,
			HttpServletRequest request){
		Optional<Employee> c = employeeRepository.findById(employeeId);
		if(!c.isPresent()) {
			throw new NotFoundException("Employee not found in database");
		}
		Optional<SalesInvoice> i = salesInvoiceRepository.findById(salesInvoiceId);
		if(!i.isPresent()) {
			throw new NotFoundException("Invoice not found in database");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/debtAllocations/allocate").toUriString());
		return ResponseEntity.created(uri).body(debtAllocationService.allocate(c.get(), i.get(), request));
	}
}
