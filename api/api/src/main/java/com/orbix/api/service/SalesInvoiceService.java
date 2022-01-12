/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Customer;
import com.orbix.api.domain.SalesInvoice;
import com.orbix.api.domain.SalesInvoiceDetail;
import com.orbix.api.models.SalesInvoiceDetailModel;
import com.orbix.api.models.SalesInvoiceModel;

/**
 * @author GODFREY
 *
 */
public interface SalesInvoiceService {
	SalesInvoiceModel save(SalesInvoice salesInvoice);
	SalesInvoiceModel get(Long id);
	SalesInvoiceModel getByNo(String no);
	boolean delete(SalesInvoice salesInvoice);
	List<SalesInvoiceModel>getAllVisible();
	List<SalesInvoiceModel>getByCustomerAndApprovedOrPartial(Customer customer);
	SalesInvoiceDetailModel saveDetail(SalesInvoiceDetail salesInvoiceDetail);
	SalesInvoiceDetailModel getDetail(Long id);
	boolean deleteDetail(SalesInvoiceDetail salesInvoiceDetail);
	List<SalesInvoiceDetailModel>getAllDetails(SalesInvoice salesInvoice);	
	boolean archive(SalesInvoice salesInvoice);
	boolean archiveAll();
	SalesInvoiceModel post(SalesInvoice salesInvoice);
}
