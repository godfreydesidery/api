/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.ProductPriceChange;

/**
 * @author GODFREY
 *
 */
public interface ProductPriceChangeRepository extends JpaRepository<ProductPriceChange, Long> {

}
