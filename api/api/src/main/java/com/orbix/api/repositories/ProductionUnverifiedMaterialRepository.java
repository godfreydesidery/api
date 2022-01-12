/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Material;
import com.orbix.api.domain.Production;
import com.orbix.api.domain.ProductionUnverifiedMaterial;

/**
 * @author GODFREY
 *
 */
public interface ProductionUnverifiedMaterialRepository extends JpaRepository<ProductionUnverifiedMaterial, Long> {

	/**
	 * @param production
	 * @param material
	 * @return
	 */
	Optional<ProductionUnverifiedMaterial> findByProductionAndMaterial(Production production, Material material);

	/**
	 * @param material
	 * @param production
	 * @return
	 */
	Optional<ProductionUnverifiedMaterial> findByMaterialAndProduction(Material material, Production production);
	
	/**
	 * @param production
	 * @return
	 */
	List<ProductionUnverifiedMaterial> findByProduction(Production production);

}
