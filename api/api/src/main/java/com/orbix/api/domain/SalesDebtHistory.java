/**
 * 
 */
package com.orbix.api.domain;

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
@Table(name = "sales_debt_histories")
public class SalesDebtHistory {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotNull
	private double cr = 0;
	@NotNull
	private double dr = 0;
	@NotNull
	private double balance = 0;
	
	private String reference;
	
	private Long createdBy;
	private Long createdAt;
	
	@ManyToOne(targetEntity = SalesDebt.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "sales_debt_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private SalesDebt salesDebt;
	
}
