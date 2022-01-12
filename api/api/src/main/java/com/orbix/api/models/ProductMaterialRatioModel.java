/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Material;
import com.orbix.api.domain.Product;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class ProductMaterialRatioModel {	
	Long id = null;
	double ratio = 0;
	String created = "";
    Product product = null;
    Material material = null;
}
