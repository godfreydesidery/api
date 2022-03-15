/**
 * 
 */
package com.orbix.api.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

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
@Table(name = "sales_debts")
public class SalesDebt {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@NotBlank
	@Column(unique = true)
	private String no;
	private String status;
	@NotNull
	private double amount = 0;
	@NotNull
	private double balance = 0;
	
	private Long createdBy;
	private Long createdAt;
	
	@ManyToOne(targetEntity = Employee.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "employee_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Employee employee;
	
	@ManyToOne(targetEntity = Customer.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "customer_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Customer customer;
	
	@OneToOne(targetEntity = SalesList.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "sales_list_id", nullable = true , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private SalesList salesList;
}
