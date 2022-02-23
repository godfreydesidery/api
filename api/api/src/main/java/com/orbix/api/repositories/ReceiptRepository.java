/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.QuotationDetail;
import com.orbix.api.domain.Receipt;

/**
 * @author GODFREY
 *
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

}
