/**
 * 
 */
package com.orbix.api.models;

import java.util.List;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class ProductionModel {
	Long id = null;
	String no = "";
	String productionName = "";
	String batchNo = "";
	double batchSize = 0;
	String uom = "";
	String status = "";	
	String comments = "";
	String created = "";
	String opened = "";
	String closed = "";
	
	List<ProductionProductModel> productionProducts;
	List<ProductionUnverifiedProductModel> productionUnverifiedProducts;
	List<ProductionMaterialModel> productionMaterials;
	List<ProductionUnverifiedMaterialModel> productionUnverifiedMaterials;
}
