/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Day;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductStock;

/**
 * @author GODFREY
 *
 */
public interface ProductStockService {
	boolean openStock(List<Product> products, Day day);
	boolean closeStock(List<Product> products, Day day);
	void saveStock(ProductStock stock);
}
