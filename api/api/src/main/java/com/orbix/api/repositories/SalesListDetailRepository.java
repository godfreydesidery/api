/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.SalesList;
import com.orbix.api.domain.SalesListDetail;

/**
 * @author GODFREY
 *
 */
public interface SalesListDetailRepository extends JpaRepository<SalesListDetail, Long> {

	/**
	 * @param pcl
	 * @return
	 */
	List<SalesListDetail> findBySalesList(SalesList pcl);

	/**
	 * @param salesList
	 * @param product
	 * @return
	 */
	Optional<SalesListDetail> findBySalesListAndProduct(SalesList salesList, Product product);

}
