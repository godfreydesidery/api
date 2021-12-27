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

import com.orbix.api.domain.Customer;
import com.orbix.api.domain.Supplier;
import com.orbix.api.service.SupplierService;

import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SupplierResource {
	
	private final 	SupplierService supplierService;
	
	@GetMapping("/suppliers")
	@PreAuthorize("hasAnyAuthority('SUPPLIER-READ')")
	public ResponseEntity<List<Supplier>>getSuppliers(){
		return ResponseEntity.ok().body(supplierService.getAll());
	}
	
	@GetMapping("/suppliers/get_names")
	public ResponseEntity<List<String>> getSupplierNames(){
//		ArrayList<String> names = new ArrayList<String>();
//		List<Supplier> s = supplierService.getAll();
//		for(Supplier a : s) {
//			names.add(a.getName());
//		}
		List<String> names = new ArrayList<String>();
		names = supplierService.getNames();
		return ResponseEntity.ok().body(names);
	}
	
	@GetMapping("/suppliers/get")
	@PreAuthorize("hasAnyAuthority('SUPPLIER-READ')")
	public ResponseEntity<Supplier> getSupplier(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(supplierService.get(id));
	}
	
	@GetMapping("/suppliers/get_by_name")
	@PreAuthorize("hasAnyAuthority('SUPPLIER-READ')")
	public ResponseEntity<Supplier> getSupplierByName(
			@RequestParam(name = "name") String name){
		return ResponseEntity.ok().body(supplierService.getByName(name));
	}
	
	@PostMapping("/suppliers/create")
	@PreAuthorize("hasAnyAuthority('SUPPLIER-CREATE')")
	public ResponseEntity<Supplier>createSupplier(
			@RequestBody Supplier supplier){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/suppliers/create").toUriString());
		return ResponseEntity.created(uri).body(supplierService.save(supplier));
	}
		
	@PutMapping("/suppliers/update")
	@PreAuthorize("hasAnyAuthority('SUPPLIER-UPDATE')")
	public ResponseEntity<Supplier>updateSupplier(
			@RequestBody Supplier supplier, 
			HttpServletRequest request){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/suppliers/update").toUriString());
		return ResponseEntity.created(uri).body(supplierService.save(supplier));
	}
	
	@DeleteMapping("/suppliers/delete")
	@PreAuthorize("hasAnyAuthority('SUPPLIER-DELETE')")
	public ResponseEntity<Boolean> deleteSupplier(
			@RequestParam(name = "id") Long id){
		Supplier supplier = supplierService.get(id);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/suppliers/delete").toUriString());
		return ResponseEntity.created(uri).body(supplierService.delete(supplier));
	}

}
