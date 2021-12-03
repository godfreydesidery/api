package com.orbix.api.models;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

/**
 * @author GODFREY
 *
 */
@Entity
@Component
@Table(name = "company_profile")
@EntityListeners(AuditingEntityListener.class)
public class CompanyProfile {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String name;
	private String companyKey;
	private String contactName;
	private String tin;
	private String vrn;
	private String physicalAddress;
	private String postCode;
	private String postAddress;
	private String telephone;
	private String mobile;
	private String email;
	private String fax;
	@Lob
	private Byte[] logo;
	private String bankAccountName;
	private String bankAddress;
	private String bankPostCode;
	private String bankName;
	private String bankAccountNo;	


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompanyKey() {
		return companyKey;
	}
	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getTin() {
		return tin;
	}
	public void setTin(String tin) {
		this.tin = tin;
	}
	public String getVrn() {
		return vrn;
	}
	public void setVrn(String vrn) {
		this.vrn = vrn;
	}
	public String getPhysicalAddress() {
		return physicalAddress;
	}
	public void setPhysicalAddress(String physicalAddress) {
		this.physicalAddress = physicalAddress;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getPostAddress() {
		return postAddress;
	}
	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public Byte[] getLogo() {
		return logo;
	}
	public void setLogo(Byte[] logo) {
		this.logo = logo;
	}
	public String getBankAccountName() {
		return bankAccountName;
	}
	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
	public String getBankAddress() {
		return bankAddress;
	}
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}
	public String getBankPostCode() {
		return bankPostCode;
	}
	public void setBankPostCode(String bankPostCode) {
		this.bankPostCode = bankPostCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAccountNo() {
		return bankAccountNo;
	}
	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}
		
}
