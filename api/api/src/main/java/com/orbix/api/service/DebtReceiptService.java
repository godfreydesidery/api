/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.DebtReceipt;
import com.orbix.api.models.DebtReceiptModel;

/**
 * @author GODFREY
 *
 */
public interface DebtReceiptService {
	DebtReceiptModel save(DebtReceipt debtReceipt);
	DebtReceiptModel get(Long id);
	DebtReceiptModel getByNo(String no);
	boolean delete(DebtReceipt debtReceipt);
	List<DebtReceiptModel>getAllVisible();	
	boolean archive(DebtReceipt debtReceipt);
	boolean archiveAll();
	DebtReceiptModel approve(DebtReceipt debtReceipt);
}
