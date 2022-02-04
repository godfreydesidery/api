/**
 * 
 */
package com.orbix.api.api;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orbix.api.domain.Employee;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.DebtModel;
import com.orbix.api.repositories.EmployeeRepository;
import com.orbix.api.repositories.DebtReceiptRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.DebtRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.DebtReceiptService;
import com.orbix.api.service.DebtService;
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
public class DebtResource {
	
	private final 	UserService userService;
	private final 	DayService dayService;
	private final 	DebtService debtService;
	private final 	DebtRepository debtRepository;
	private final 	EmployeeRepository employeeRepository;
	private final 	ProductRepository productRepository;
	
	@GetMapping("/debts/employee")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-READ')")
	public ResponseEntity<List<DebtModel>>getDebts(
			@RequestParam(name = "id") Long id){
		Optional<Employee> e = employeeRepository.findById(id);
		if(!e.isPresent()) {
			throw new NotFoundException("Employee not found in database");
		}
		return ResponseEntity.ok().body(debtService.getByEmployeeAndApprovedOrPartial(e.get()));
	}
}
