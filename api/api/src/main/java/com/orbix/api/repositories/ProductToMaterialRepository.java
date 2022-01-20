/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Lpo;
import com.orbix.api.domain.ProductToMaterial;

/**
 * @author GODFREY
 *
 */
public interface ProductToMaterialRepository extends JpaRepository<ProductToMaterial, Long> {

	/**
	 * @param no
	 * @return
	 */
	Optional<ProductToMaterial> findByNo(String no);

	/**
	 * @param statuses
	 * @return
	 */
	@Query("SELECT p FROM ProductToMaterial p WHERE p.status IN (:statuses)")
	List<ProductToMaterial> findAllVissible(List<String> statuses);
	
	@Query("SELECT p FROM ProductToMaterial p WHERE p.status =:status")
	List<ProductToMaterial> findAllApproved(String status);

}
