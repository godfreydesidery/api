/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Quotation;
import com.orbix.api.domain.QuotationDetail;
import com.orbix.api.models.QuotationDetailModel;
import com.orbix.api.models.QuotationModel;

/**
 * @author GODFREY
 *
 */
public interface QuotationService {
	QuotationModel save(Quotation quotation);
	QuotationModel get(Long id);
	QuotationModel getByNo(String no);
	boolean delete(Quotation quotation);
	List<QuotationModel>getAllVisible();
	QuotationDetailModel saveDetail(QuotationDetail quotationDetail);
	QuotationDetailModel getDetail(Long id);
	boolean deleteDetail(QuotationDetail quotationDetail);
	List<QuotationDetailModel>getAllDetails(Quotation quotation);	
	boolean archive(Quotation quotation);
	boolean archiveAll();
}
