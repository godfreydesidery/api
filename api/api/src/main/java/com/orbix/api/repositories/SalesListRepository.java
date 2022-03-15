/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.PackingList;
import com.orbix.api.domain.SalesList;

/**
 * @author GODFREY
 *
 */
public interface SalesListRepository extends JpaRepository<SalesList, Long> {

	/**
	 * @param no
	 * @return
	 */
	Optional<SalesList> findByNo(String no);
	
	@Query("SELECT s FROM SalesList s WHERE s.status IN (:statuses)")
	List<SalesList> findAllVissible(List<String> statuses);
	
	@Query("SELECT s FROM SalesList s WHERE s.status =:status")
	List<SalesList> findAllPosted(String status);

}
