/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.CompanyProfile;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.repositories.CompanyProfileRepository;
import com.orbix.api.repositories.TillRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GODFREY
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CompanyProfileServiceImpl implements CompanyProfileService {
	
	private final CompanyProfileRepository companyProfileRepository;

	@Override
	public CompanyProfile saveCompanyProfile(CompanyProfile companyProfile) {
		if(validateCompany(companyProfile)) {
			companyProfileRepository.deleteAll();
			return companyProfileRepository.saveAndFlush(companyProfile);
		}else {
			throw new InvalidEntryException("Invalid company information");
		}
	}
	
	private boolean validateCompany(CompanyProfile profile) {
		/**
		 * Add validation logic, return true if valid, else false
		 */
		
		return true;
	}

	@Override
	public CompanyProfile getCompanyProfile() {
		List<CompanyProfile> profiles = companyProfileRepository.findAll();
		CompanyProfile profile = new CompanyProfile();
		for(CompanyProfile p : profiles) {
			profile = p;
		}
		return profile;
 	}

}
