/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Material;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductMaterialRatio;

/**
 * @author GODFREY
 *
 */
public interface ProductMaterialRatioRepository extends JpaRepository<ProductMaterialRatio, Long> {

	/**
	 * @param product
	 * @param material
	 * @return
	 */
	Optional<ProductMaterialRatio> findByProductAndMaterial(Product product, Material material);

	/**
	 * @param product
	 * @return
	 */
	Optional<ProductMaterialRatio> findByProduct(Product product);

	/**
	 * @param material
	 * @return
	 */
	List<Material> findByMaterial(Material material);

}
