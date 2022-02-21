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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String barcode;
	@NotBlank
	@Column(unique = true)
	private String code;
	@NotBlank
	@Column(unique = true)
	private String description;
	@Column(unique = true)
	private String shortDescription;
	@Column(unique = true)
	private String commonName;
	private double discount = 0;
	private double vat = 0;
	private double profitMargin = 0;
	private double costPriceVatIncl = 0;
	private double costPriceVatExcl = 0;
	private double sellingPriceVatIncl = 0;
	private double sellingPriceVatExcl = 0;
	private String uom;
	private double packSize = 1;
	private double stock = 0;
	private double minimumInventory = 0;
	private double maximumInventory = 0;
	private double defaultReorderQty = 0;
	private double defaultReorderLevel = 0;
	private boolean active = true;
	private boolean sellable = true;
	private String ingredients = "";
		
	@ManyToOne(targetEntity = Supplier.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "supplier_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Supplier supplier;
	
	@ManyToOne(targetEntity = Department.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "department_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
	@JsonIgnoreProperties("department")
    private Department department;
	
	@ManyToOne(targetEntity = Class.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "class_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
	@JsonIgnoreProperties("class_")
    private Class class_;
	
	@ManyToOne(targetEntity = SubClass.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "sub_class_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
	@JsonIgnoreProperties("subClass")
    private SubClass subClass;
	
	@ManyToOne(targetEntity = Category.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "category_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
	@JsonIgnoreProperties("subCategory")
    private Category category;
	
	@ManyToOne(targetEntity = SubCategory.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "sub_category_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private SubCategory subCategory;
	
	@ManyToOne(targetEntity = LevelOne.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "level_one_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private LevelOne levelOne;
	
	@ManyToOne(targetEntity = LevelTwo.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "level_two_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private LevelTwo levelTwo;
	
	@ManyToOne(targetEntity = LevelThree.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "level_three_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private LevelThree levelThree;
	
	@ManyToOne(targetEntity = LevelFour.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "level_four_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private LevelFour levelFour;	
}
