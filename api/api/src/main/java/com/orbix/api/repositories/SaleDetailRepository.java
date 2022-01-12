/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Sale;
import com.orbix.api.domain.SaleDetail;

/**
 * @author GODFREY
 *
 */
public interface SaleDetailRepository extends JpaRepository<SaleDetail, Long> {

	/**
	 * @param sale
	 * @return
	 */
	List<SaleDetail> findBySale(Sale sale);

}
