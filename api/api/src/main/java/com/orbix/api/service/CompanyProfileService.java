/**
 * 
 */
package com.orbix.api.service;

import com.orbix.api.domain.CompanyProfile;

/**
 * @author GODFREY
 *
 */
public interface CompanyProfileService {
	CompanyProfile saveCompanyProfile(CompanyProfile companyProfile);
	CompanyProfile getCompanyProfile();
}
