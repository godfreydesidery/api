/**
 * 
 */
package com.orbix.api.domain;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GODFREY
 *
 */
@Entity
@Data  
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "customers")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String no;
	@NotBlank
	@Column(unique = true)
	private String name;
	@NotBlank
	private String contactName;
	private boolean active = true;
	private String tin;
	private String vrn;
	private double creditLimit = 0;
	private double invoiceLimit = 0;
	private double balance = 0;
	private int creditDays = 1;
	private String physicalAddress;
	private String postCode;
	private String postAddress;
	private String telephone;
	private String mobile;
	private String email;
	private String fax;
	private String bankAccountName;
	private String bankPhysicalAddress;
	private String bankPostCode;
	private String bankPostAddress;
	private String bankName;
	private String bankAccountNo;

}
