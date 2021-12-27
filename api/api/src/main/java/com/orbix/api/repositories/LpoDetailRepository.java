/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Lpo;
import com.orbix.api.domain.LpoDetail;
import com.orbix.api.domain.Product;

/**
 * @author GODFREY
 *
 */
public interface LpoDetailRepository extends JpaRepository<LpoDetail, Long> {

	/**
	 * @param lpo
	 * @return
	 */
	List<LpoDetail> findByLpo(Lpo lpo);

	/**
	 * @param lpo
	 * @param product
	 * @return
	 */
	Optional<LpoDetail> findByLpoAndProduct(Lpo lpo, Product product);

}
