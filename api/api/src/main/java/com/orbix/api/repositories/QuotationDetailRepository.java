/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.Quotation;
import com.orbix.api.domain.QuotationDetail;

/**
 * @author GODFREY
 *
 */
public interface QuotationDetailRepository extends JpaRepository<QuotationDetail, Long> {

	/**
	 * @param quotation
	 * @return
	 */
	List<QuotationDetail> findByQuotation(Quotation quotation);

	/**
	 * @param quotation
	 * @param product
	 * @return
	 */
	Optional<QuotationDetail> findByQuotationAndProduct(Quotation quotation, Product product);

}
