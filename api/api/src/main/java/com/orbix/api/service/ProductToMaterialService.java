/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Customer;
import com.orbix.api.domain.ProductToMaterial;
import com.orbix.api.domain.ProductToMaterialDetail;
import com.orbix.api.models.ProductToMaterialDetailModel;
import com.orbix.api.models.ProductToMaterialModel;

/**
 * @author GODFREY
 *
 */
public interface ProductToMaterialService {
	ProductToMaterialModel save(ProductToMaterial productToMaterial);
	ProductToMaterialModel get(Long id);
	ProductToMaterialModel getByNo(String no);
	boolean delete(ProductToMaterial productToMaterial);
	List<ProductToMaterialModel>getAllVisible();
	List<ProductToMaterialModel>getByCustomerAndApprovedOrPartial(Customer customer);
	ProductToMaterialDetailModel saveDetail(ProductToMaterialDetail productToMaterialDetail);
	ProductToMaterialDetailModel getDetail(Long id);
	boolean deleteDetail(ProductToMaterialDetail productToMaterialDetail);
	List<ProductToMaterialDetailModel>getAllDetails(ProductToMaterial productToMaterial);	
	boolean archive(ProductToMaterial productToMaterial);
	boolean archiveAll();
	ProductToMaterialModel post(ProductToMaterial productToMaterial);
}
