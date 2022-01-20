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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
@Table(name = "productions")
public class Production {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String no;
	private String productionName;
	@Column(unique = true)
	private String batchNo;
	private double batchSize;
	private String uom;
	private String status;
	private String comments;
	
	private Long createdBy;
	private Long createdAt;
	private Long openedBy;
	private Long openedAt;
	private Long closedBy;
	private Long closedAt;
	
	@OneToMany(targetEntity = ProductionProduct.class, mappedBy = "production", fetch = FetchType.LAZY, orphanRemoval = true)
    @Valid
    @JsonIgnoreProperties("production")
    private List<ProductionProduct> productionProducts;
	
	@OneToMany(targetEntity = ProductionUnverifiedProduct.class, mappedBy = "production", fetch = FetchType.LAZY, orphanRemoval = true)
    @Valid
    @JsonIgnoreProperties("production")
    private List<ProductionUnverifiedProduct> productionUnverifiedProducts;
	
	@OneToMany(targetEntity = ProductionMaterial.class, mappedBy = "production", fetch = FetchType.LAZY, orphanRemoval = true)
    @Valid
    @JsonIgnoreProperties("production")
    private List<ProductionMaterial> productionMaterials;
	
	@OneToMany(targetEntity = ProductionUnverifiedMaterial.class, mappedBy = "production", fetch = FetchType.LAZY, orphanRemoval = true)
    @Valid
    @JsonIgnoreProperties("production")
    private List<ProductionUnverifiedMaterial> productionUnverifiedMaterials;
	
}
