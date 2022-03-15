/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.SalesList;
import com.orbix.api.domain.SalesListDetail;
import com.orbix.api.models.SalesListDetailModel;
import com.orbix.api.models.SalesListModel;

/**
 * @author GODFREY
 *
 */
public interface SalesListService {
	SalesListModel save(SalesList salesList);
	SalesListModel get(Long id);
	SalesListModel getByNo(String no);
	boolean delete(SalesList salesList);
	List<SalesListModel>getAllVisible();
	SalesListDetailModel saveDetail(SalesListDetail salesListDetail);
	SalesListDetailModel getDetail(Long id);
	List<SalesListDetailModel>getAllDetails(SalesList salesList);	
	boolean archive(SalesList salesList);
	boolean archiveAll();
	SalesListModel approve(SalesList salesList, HttpServletRequest request);
	String generateSalesListNo(SalesList salesList);
}
