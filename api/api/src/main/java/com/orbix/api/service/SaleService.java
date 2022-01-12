/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Sale;
import com.orbix.api.domain.SaleDetail;
import com.orbix.api.models.SaleDetailModel;
import com.orbix.api.models.SaleModel;

/**
 * @author GODFREY
 *
 */
public interface SaleService {
	SaleModel save(Sale sale);
	SaleModel get(Long id);
	boolean delete(Sale sale);
	List<SaleModel>getAll();
	SaleDetailModel saveDetail(SaleDetail saleDetail);
	SaleDetailModel getDetail(Long id);
	boolean deleteDetail(SaleDetail saleDetail);
	List<SaleDetailModel>getAllDetails(Sale sale);	
}
