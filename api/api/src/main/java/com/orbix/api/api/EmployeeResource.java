/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.ArrayList;
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

import com.orbix.api.domain.Employee;
import com.orbix.api.service.EmployeeService;

import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeResource {

	private final 	EmployeeService employeeService;
	
	@GetMapping("/employees")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE-READ')")
	public ResponseEntity<List<Employee>>getEmployees(){
		return ResponseEntity.ok().body(employeeService.getAll());
	}
	
	@GetMapping("/employees/get_aliases")
	public ResponseEntity<List<String>> getEmployeeNames(){
		List<String> names = new ArrayList<String>();
		names = employeeService.getAliases();
		return ResponseEntity.ok().body(names);
	}
	
	@GetMapping("/employees/get")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE-READ')")
	public ResponseEntity<Employee> getEmployee(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(employeeService.get(id));
	}
	
	@GetMapping("/employees/get_by_no")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE-READ')")
	public ResponseEntity<Employee> getEmployeeByNo(
			@RequestParam(name = "no") String no){
		return ResponseEntity.ok().body(employeeService.getByNo(no));
	}
	
	@GetMapping("/employees/get_by_alias")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE-READ')")
	public ResponseEntity<Employee> getEmployeeByName(
			@RequestParam(name = "alias") String name){
		return ResponseEntity.ok().body(employeeService.getByName(name));
	}
	
	@PostMapping("/employees/create")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE-CREATE')")
	public ResponseEntity<Employee>createEmployee(
			@RequestBody Employee employee){
		employee.setNo("NA");
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/employees/create").toUriString());
		return ResponseEntity.created(uri).body(employeeService.save(employee));
	}
		
	@PutMapping("/employees/update")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE-UPDATE')")
	public ResponseEntity<Employee>updateEmployee(
			@RequestBody Employee employee, 
			HttpServletRequest request){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/employees/update").toUriString());
		return ResponseEntity.created(uri).body(employeeService.save(employee));
	}
	
	@DeleteMapping("/employees/delete")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE-DELETE')")
	public ResponseEntity<Boolean> deleteEmployee(
			@RequestParam(name = "id") Long id){
		Employee employee = employeeService.get(id);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/employees/delete").toUriString());
		return ResponseEntity.created(uri).body(employeeService.delete(employee));
	}
}
