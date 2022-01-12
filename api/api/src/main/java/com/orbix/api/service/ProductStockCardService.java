/**
 * 
 */
package com.orbix.api.service;

import java.time.LocalDate;
import java.util.List;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductStockCard;

/**
 * @author GODFREY
 *
 */
public interface ProductStockCardService {
	ProductStockCard save(ProductStockCard stockCard);
	List<ProductStockCard>getAll();
	List<ProductStockCard>getByProduct(Product product);
	List<ProductStockCard>getByDate(LocalDate startDate, LocalDate endDate);
}
