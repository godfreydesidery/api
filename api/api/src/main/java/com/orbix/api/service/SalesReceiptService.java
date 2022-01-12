/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.SalesInvoice;
import com.orbix.api.domain.SalesReceipt;
import com.orbix.api.models.SalesInvoiceModel;
import com.orbix.api.models.SalesReceiptModel;

/**
 * @author GODFREY
 *
 */
public interface SalesReceiptService {
	SalesReceiptModel save(SalesReceipt salesReceipt);
	SalesReceiptModel get(Long id);
	SalesReceiptModel getByNo(String no);
	boolean delete(SalesReceipt salesReceipt);
	List<SalesReceiptModel>getAllVisible();	
	boolean archive(SalesReceipt salesReceipt);
	boolean archiveAll();
	SalesReceiptModel approve(SalesReceipt salesReceipt);
}
