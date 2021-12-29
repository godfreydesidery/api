/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Grn;
import com.orbix.api.domain.GrnDetail;
import com.orbix.api.models.GrnDetailModel;
import com.orbix.api.models.GrnModel;

/**
 * @author GODFREY
 *
 */
public interface GrnService {
	GrnModel save(Grn grn);
	GrnModel get(Long id);
	GrnModel getByNo(String no);
	boolean delete(Grn grn);
	List<GrnModel>getAllVisible();
	GrnDetailModel saveDetail(GrnDetail grnDetail);
	GrnDetailModel getDetail(Long id);
	List<GrnDetailModel>getAllDetails(Grn grn);	
}
