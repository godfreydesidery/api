/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.SalesInvoice;
import com.orbix.api.domain.SalesInvoiceDetail;

/**
 * @author GODFREY
 *
 */
public interface SalesInvoiceDetailRepository extends JpaRepository<SalesInvoiceDetail, Long> {

	/**
	 * @param salesInvoice
	 * @return
	 */
	List<SalesInvoiceDetail> findBySalesInvoice(SalesInvoice salesInvoice);

	/**
	 * @param salesInvoice
	 * @param product
	 * @return
	 */
	Optional<SalesInvoiceDetail> findBySalesInvoiceAndProduct(SalesInvoice salesInvoice, Product product);

}
