/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.ProductStockCard;

/**
 * @author GODFREY
 *
 */
public interface ProductStockCardRepository extends JpaRepository<ProductStockCard, Long> {

}
