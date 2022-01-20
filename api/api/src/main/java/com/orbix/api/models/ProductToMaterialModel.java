/**
 * 
 */
package com.orbix.api.models;

import java.util.List;

import com.orbix.api.domain.ProductToMaterialDetail;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class ProductToMaterialModel {
	Long id = null;
	String no = "";
	String status = "";
	String comments = "";
	String created = "";
	String approved = "";
	
	List<ProductToMaterialDetailModel> productToMaterialDetails;
}
