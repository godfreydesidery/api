/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Customer;
import com.orbix.api.domain.SalesInvoice;

/**
 * @author GODFREY
 *
 */
public interface SalesInvoiceRepository extends JpaRepository<SalesInvoice, Long> {

	/**
	 * @param no
	 * @return
	 */
	Optional<SalesInvoice> findByNo(String no);
	
	@Query("SELECT i FROM SalesInvoice i WHERE i.status IN (:statuses)")
	List<SalesInvoice> findAllVissible(List<String> statuses);

	/**
	 * @param customer
	 * @param string
	 * @return
	 */
	@Query("SELECT i FROM SalesInvoice i WHERE i.customer =:customer AND i.status IN (:statuses)")
	List<SalesInvoice> findByCustomerAndApprovedOrPosted(Customer customer, List<String> statuses);
	
}
