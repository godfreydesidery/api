/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Product;

/**
 * @author GODFREY
 *
 */
public interface ProductService {
	Product save(Product product);
	Product get(Long id);
	Product getByBarcode(String barcode);
	Product getByCode(String code);
	Product getByDescription(String description);
	Product getByCommonName(String commonName);
	boolean delete(Product product);
	List<Product>getAll();	
	List<String> getSellableDescriptions();
}
