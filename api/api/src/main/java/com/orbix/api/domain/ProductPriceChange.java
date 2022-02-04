/**
 * 
 */
package com.orbix.api.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "product_price_changes")
public class ProductPriceChange {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private double originalCostPriceVatIncl = 0;
	private double originalCostPriceVatExcl = 0;
	private double originalSellingPriceVatExcl = 0;
	private double originalSellingPriceVatIncl = 0;
	
	private double finalCostPriceVatIncl = 0;
	private double finalCostPriceVatExcl = 0;
	private double finalSellingPriceVatExcl = 0;
	private double finalSellingPriceVatIncl = 0;
	
	private String reference;
	@Temporal(TemporalType.TIMESTAMP)
    private Date dateTime = new Date();
	
	@ManyToOne(targetEntity = Product.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "product_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Product product;
	
	@ManyToOne(targetEntity = Day.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "day_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Day day;
}
