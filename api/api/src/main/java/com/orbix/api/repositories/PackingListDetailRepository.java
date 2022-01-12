/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PackingList;
import com.orbix.api.domain.PackingListDetail;
import com.orbix.api.domain.Product;

/**
 * @author GODFREY
 *
 */
public interface PackingListDetailRepository extends JpaRepository<PackingListDetail, Long> {

	/**
	 * @param packingList
	 * @return
	 */
	List<PackingListDetail> findByPackingList(PackingList packingList);

	/**
	 * @param packingList
	 * @param product
	 * @return
	 */
	Optional<PackingListDetail> findByPackingListAndProduct(PackingList packingList, Product product);

}
