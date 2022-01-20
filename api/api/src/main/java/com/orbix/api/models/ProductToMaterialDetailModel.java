/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Material;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductToMaterial;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class ProductToMaterialDetailModel {
	Long id = null;
	double qty = 0;
	double ratio = 0;
	Product product = null;
	Material material = null;
	ProductToMaterial productToMaterial;
}
