/**
 * 
 */
package com.orbix.api.service;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javax.transaction.Transactional;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
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
		List<CompanyProfile> profiles = companyProfileRepository.findAll();
		int i = 0;
		CompanyProfile profile = new CompanyProfile();
		for(CompanyProfile p : profiles) {
			i = i + 1;
			profile = p;
		}
		if(validateCompany(companyProfile)) {
			if(i > 1) {
				companyProfileRepository.deleteAll();
			}else if(companyProfile.getId() == null) {
				companyProfileRepository.deleteAll();
			}
			if(i == 1) {
				profile.setCompanyName(companyProfile.getCompanyName());
				profile.setContactName(companyProfile.getContactName());
				profile.setTin(companyProfile.getTin());
				profile.setVrn(companyProfile.getVrn());
				profile.setPhysicalAddress(companyProfile.getPhysicalAddress());
				profile.setPostCode(companyProfile.getPostCode());
				profile.setPostAddress(companyProfile.getPostAddress());
				profile.setTelephone(companyProfile.getTelephone());
				profile.setMobile(companyProfile.getMobile());
				profile.setEmail(companyProfile.getEmail());
				profile.setWebsite(companyProfile.getWebsite());
				profile.setFax(companyProfile.getFax());
				profile.setBankAccountName(companyProfile.getBankAccountName());
				profile.setBankPhysicalAddress(companyProfile.getBankPhysicalAddress());
				profile.setBankPostAddress(companyProfile.getBankPostAddress());
				profile.setBankPostCode(companyProfile.getBankPostCode());
				profile.setBankName(companyProfile.getBankName());
				profile.setBankAccountNo(companyProfile.getBankAccountNo());
			}else {
				profile = companyProfile;
			}
			return companyProfileRepository.saveAndFlush(profile);
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
			//profile.setLogo(decompressBytes(p.getLogo()));
			break;
		}	
		return profile;
 	}
	
	public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
	
	@Override
	public boolean hasData() {
		return companyProfileRepository.hasData();
	}

}
