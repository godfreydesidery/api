/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Quotation;

/**
 * @author GODFREY
 *
 */
public interface QuotationRepository extends JpaRepository<Quotation, Long> {

	/**
	 * @param no
	 * @return
	 */
	Optional<Quotation> findByNo(String no);
	
	@Query("SELECT q FROM Quotation q WHERE q.status IN (:statuses)")
	List<Quotation> findAllVissible(List<String> statuses);

}
