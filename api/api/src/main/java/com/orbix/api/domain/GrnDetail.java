/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
@Table(name = "grn_details")
public class GrnDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull	
	private double qtyOrdered;
	private double qtyReceived = 0;
	private double supplierPriceVatIncl;
	private double supplierPriceVatExcl;
	private double clientPriceVatIncl;
	private double clientPriceVatExcl;
	
	@ManyToOne(targetEntity = Product.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "product_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Product product;
	
	@ManyToOne(targetEntity = Grn.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "grn_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Grn grn;
}
