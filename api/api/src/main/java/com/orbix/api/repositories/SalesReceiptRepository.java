/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.SalesReceipt;

/**
 * @author GODFREY
 *
 */
public interface SalesReceiptRepository extends JpaRepository<SalesReceipt, Long> {

	/**
	 * @param no
	 * @return
	 */
	Optional<SalesReceipt> findByNo(String no);
	
	@Query("SELECT r FROM SalesReceipt r WHERE r.status IN (:statuses)")
	List<SalesReceipt> findAllVissible(List<String> statuses);
	
	@Query("SELECT r FROM SalesReceipt r WHERE r.status =:status")
	List<SalesReceipt> findAllApproved(String status);

}
