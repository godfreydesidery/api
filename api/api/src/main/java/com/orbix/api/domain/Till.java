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
@Table(name = "tills")
public class Till {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String no;
	@Column(unique = true)
	private String computerName;
	private boolean active = true;
	
	private double cash = 0;
	private double voucher = 0;
	private double deposit = 0;
	private double loyalty = 0;
	private double crCard = 0;
	private double cheque = 0;
	private double cap = 0;
	private double invoice = 0;
	private double crNote = 0;
	private double mobile = 0;
	private double other = 0;
	
	private double floatBalance = 0;
}
