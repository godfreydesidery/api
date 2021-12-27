/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Day;
import com.orbix.api.domain.Material;
import com.orbix.api.domain.MaterialStock;

/**
 * @author GODFREY
 *
 */
public interface MaterialStockRepository extends JpaRepository<MaterialStock, Long> {

	/**
	 * @param p
	 * @param day
	 * @return
	 */
	Optional<MaterialStock> findByMaterialAndDay(Material p, Day day);

}
