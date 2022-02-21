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

import com.orbix.api.domain.Till;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.service.TillService;
import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TillResource {
	
	private final 	TillService tillService;
	
	@GetMapping("/tills")
	@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<List<Till>>getTills(){
		return ResponseEntity.ok().body(tillService.getTills());
	}
	
	@GetMapping("/tills/get")
	@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<Till> getTill(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(tillService.getTill(id));
	}
	
	@GetMapping("/tills/get_by_till_no")
	@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<Till> getTillByTillNo(
			@RequestParam(name = "till_no") String no){
		return ResponseEntity.ok().body(tillService.getTillByNo(no));
	}
	
	@GetMapping("/tills/get_by_computer_name")
	public ResponseEntity<Till> getTillByComputerName(
			@RequestParam(name = "computer_name") String computerName){
		return ResponseEntity.ok().body(tillService.getTillByComputerName(computerName));
	}
	
	@PostMapping("/tills/create")
	@PreAuthorize("hasAnyAuthority('TILL-CREATE')")
	public ResponseEntity<Till>createTill(
			@RequestBody Till till){
		till.setActive(true);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/tills/create").toUriString());
		return ResponseEntity.created(uri).body(tillService.saveTill(till));
	}
		
	@PutMapping("/tills/update")
	@PreAuthorize("hasAnyAuthority('USER-UPDATE')")
	public ResponseEntity<Till>updateTill(
			@RequestBody Till till, 
			HttpServletRequest request){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/tills/update").toUriString());
		return ResponseEntity.created(uri).body(tillService.saveTill(till));
	}
	
	@DeleteMapping("/tills/delete")
	@PreAuthorize("hasAnyAuthority('TILL-DELETE')")
	public ResponseEntity<Boolean> deleteTill(
			@RequestParam(name = "id") Long id){
		Till till = tillService.getTill(id);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/tills/delete").toUriString());
		return ResponseEntity.created(uri).body(tillService.deleteTill(till));
	}
	
	@PostMapping("/tills/activate")
	//@PreAuthorize("hasAnyAuthority('TILL-DELETE')")
	public ResponseEntity<Till> activateTill(
			@RequestParam(name = "id") Long id){
		Till till = tillService.getTill(id);
		till.setActive(true);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/tills/activate").toUriString());
		return ResponseEntity.created(uri).body(tillService.saveTill(till));
	}
	
	@PostMapping("/tills/deactivate")
	//@PreAuthorize("hasAnyAuthority('TILL-DELETE')")
	public ResponseEntity<Till> deactivateTill(
			@RequestParam(name = "id") Long id){
		Till till = tillService.getTill(id);
		till.setActive(false);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/tills/deactivate").toUriString());
		return ResponseEntity.created(uri).body(tillService.saveTill(till));
	}

}
