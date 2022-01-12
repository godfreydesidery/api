/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
@Table(name = "sales_receipt")
public class SalesReceipt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String no;
	private String status;
	private LocalDate receiptDate;
	private String mode;
	private double amount;
	private String chequeNo;	
	private String comments;
	
	private Long createdBy;
	private Long createdAt;
	private Long approvedBy;
	private Long approvedAt;
	
	@ManyToOne(targetEntity = Customer.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Customer customer;
}
