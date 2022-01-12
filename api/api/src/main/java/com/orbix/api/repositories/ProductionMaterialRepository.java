/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Material;
import com.orbix.api.domain.Production;
import com.orbix.api.domain.ProductionMaterial;

/**
 * @author GODFREY
 *
 */
public interface ProductionMaterialRepository extends JpaRepository<ProductionMaterial, Long> {

	/**
	 * @param production
	 * @return
	 */
	List<ProductionMaterial> findByProduction(Production production);

	/**
	 * @param material
	 * @param production
	 * @return
	 */
	Optional<ProductionMaterial> findByMaterialAndProduction(Material material, Production production);

}
