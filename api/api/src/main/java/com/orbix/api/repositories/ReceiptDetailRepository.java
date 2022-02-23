/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.QuotationDetail;
import com.orbix.api.domain.ReceiptDetail;

/**
 * @author GODFREY
 *
 */
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long> {

}
