/**
 * 
 */
package com.orbix.api.service;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductMaterialRatio;
import com.orbix.api.models.ProductMaterialRatioModel;

/**
 * @author GODFREY
 *
 */
public interface ProductMaterialRatioService {
	public ProductMaterialRatioModel save(ProductMaterialRatio productMaterialRatio);
	public ProductMaterialRatioModel get(Long id);
	public ProductMaterialRatioModel getByProduct(Product product);
	public boolean delete(Long id);
}
