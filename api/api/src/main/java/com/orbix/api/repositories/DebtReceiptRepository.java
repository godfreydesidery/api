/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.DebtReceipt;

/**
 * @author GODFREY
 *
 */
public interface DebtReceiptRepository extends JpaRepository<DebtReceipt, Long> {

	/**
	 * @param no
	 * @return
	 */
	Optional<DebtReceipt> findByNo(String no);
	
	@Query("SELECT r FROM DebtReceipt r WHERE r.status IN (:statuses)")
	List<DebtReceipt> findAllVissible(List<String> statuses);
	
	@Query("SELECT r FROM DebtReceipt r WHERE r.status =:status")
	List<DebtReceipt> findAllApproved(String status);

}
