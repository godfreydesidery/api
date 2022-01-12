/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.Production;
import com.orbix.api.domain.ProductionProduct;

/**
 * @author GODFREY
 *
 */
public interface ProductionProductRepository extends JpaRepository<ProductionProduct, Long> {

	/**
	 * @param production
	 * @return
	 */
	List<ProductionProduct> findByProduction(Production production);

	/**
	 * @param product
	 * @param production
	 * @return
	 */
	Optional<ProductionProduct> findByProductAndProduction(Product product, Production production);

}