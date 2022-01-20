/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductToMaterial;
import com.orbix.api.domain.ProductToMaterialDetail;

/**
 * @author GODFREY
 *
 */
public interface ProductToMaterialDetailRepository extends JpaRepository<ProductToMaterialDetail, Long> {

	/**
	 * @param productToMaterial
	 * @return
	 */
	List<ProductToMaterialDetail> findByProductToMaterial(ProductToMaterial productToMaterial);

	/**
	 * @param product
	 * @param productToMaterial
	 * @return
	 */
	Optional<ProductToMaterialDetail> findByProductAndProductToMaterial(Product product,
			ProductToMaterial productToMaterial);

}
