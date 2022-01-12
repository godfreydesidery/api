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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
@Table(name = "product_material_ratios")
public class ProductMaterialRatio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private double ratio;
	
	private Long createdBy;
	private Long createdAt;
	
	@OneToOne(targetEntity = Product.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "product_id", nullable = false , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Product product;
	
	@OneToOne(targetEntity = Material.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "material_id", nullable = false , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Material material;
}
