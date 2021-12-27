/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Day;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductStock;

/**
 * @author GODFREY
 *
 */
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

	/**
	 * @param p
	 * @param day
	 * @return
	 */
	Optional<ProductStock> findByProductAndDay(Product p, Day day);

}
