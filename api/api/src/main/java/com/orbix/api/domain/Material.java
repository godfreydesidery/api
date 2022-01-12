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
@Table(name = "materials")
public class Material {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String code;
	@NotBlank
	@Column(unique = true)
	private String description;
	private double vat = 0;
	private double costPriceVatIncl = 0;
	private double costPriceVatExcl = 0;
	private String uom;
	private double stock = 0;
	private double minimumInventory = 0;
	private double maximumInventory = 0;
	private double defaultReorderQty = 0;
	private double defaultReorderLevel = 0;
	private boolean active = true;
	
	@ManyToOne(targetEntity = Category.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "category_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Category category;
	
	@ManyToOne(targetEntity = SubCategory.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "sub_category_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private SubCategory subCategory;	
}
