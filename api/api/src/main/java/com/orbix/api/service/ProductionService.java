/**
 * 
 */
package com.orbix.api.service;

import com.orbix.api.domain.Material;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.Production;
import com.orbix.api.models.PackingListModel;
import com.orbix.api.models.ProductionMaterialModel;
import com.orbix.api.models.ProductionModel;
import com.orbix.api.models.ProductionProductModel;
import com.orbix.api.models.ProductionUnverifiedMaterialModel;
import com.orbix.api.models.ProductionUnverifiedProductModel;

/**
 * @author GODFREY
 *
 */
public interface ProductionService {
	public ProductionModel get(Long id);
	public ProductionModel getByNo(String no);
	public ProductionModel save(Production production);
	public ProductionModel close(Production production);
	public ProductionModel cancel(Production production);
	public ProductionUnverifiedMaterialModel addMaterial(Production production, Material material, double qty);
	public ProductionUnverifiedMaterialModel deductMaterial(Production production, Material material, double qty);
	public ProductionMaterialModel verifyMaterial(Production production, Material material, double qty);
	public boolean removeMaterial(Production production, Material material);
	public ProductionUnverifiedProductModel addProduct(Production production, Product product, double qty);
	public ProductionUnverifiedProductModel deductProduct(Production production, Product product, double qty);
	public ProductionProductModel verifyProduct(Production production, Product product, double qty);
	public boolean removeProduct(Production production, Product product);
}
