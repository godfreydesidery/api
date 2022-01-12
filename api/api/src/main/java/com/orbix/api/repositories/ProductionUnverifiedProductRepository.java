/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.Production;
import com.orbix.api.domain.ProductionUnverifiedProduct;

/**
 * @author GODFREY
 *
 */
public interface ProductionUnverifiedProductRepository extends JpaRepository<ProductionUnverifiedProduct, Long> {

	/**
	 * @param production
	 * @param product
	 * @return
	 */
	Optional<ProductionUnverifiedProduct> findByProductionAndProduct(Production production, Product product);

	/**
	 * @param product
	 * @param production
	 * @return
	 */
	Optional<ProductionUnverifiedProduct> findByProductAndProduction(Product product, Production production);

	/**
	 * @param production
	 * @return
	 */
	List<ProductionUnverifiedProduct> findByProduction(Production production);
}
