/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.domain.CompanyProfile;
import com.orbix.api.domain.Till;
import com.orbix.api.service.CompanyProfileService;
import com.orbix.api.service.TillService;

import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompanyProfileResource {
	
	private final 	CompanyProfileService companyProfileService;
	
	@GetMapping("/company_profile/get")
	public ResponseEntity<CompanyProfile> getCompanyProfile(){		
		return ResponseEntity.ok().body(companyProfileService.getCompanyProfile());
	}
	
	@PostMapping("/company_profile/save")
	@PreAuthorize("hasAnyAuthority('COMPANY_PROFILE-CREATE')")
	public ResponseEntity<CompanyProfile>saveCompanyProfile(
			@RequestBody CompanyProfile profile){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/company_profile/save").toUriString());
		return ResponseEntity.created(uri).body(companyProfileService.saveCompanyProfile(profile));
	}

}
