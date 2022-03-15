/**
 * 
 */
package com.orbix.api.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name = "sales_list_details")
public class SalesListDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	private double totalPacked = 0;
	private double qtySold = 0;
	private double qtyOffered = 0;
	private double qtyReturned = 0;
	private double qtyDamaged = 0;
	private double costPriceVatIncl;
	private double costPriceVatExcl;
	private double sellingPriceVatIncl;
	private double sellingPriceVatExcl;
	
	@ManyToOne(targetEntity = Product.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "product_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Product product;
	
	@ManyToOne(targetEntity = SalesList.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "sales_list_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private SalesList salesList;
}
