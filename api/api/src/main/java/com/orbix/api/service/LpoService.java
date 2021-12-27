/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Lpo;
import com.orbix.api.domain.LpoDetail;
import com.orbix.api.models.LpoDetailModel;
import com.orbix.api.models.LpoModel;

/**
 * @author GODFREY
 *
 */
public interface LpoService {
	LpoModel save(Lpo lpo);
	LpoModel get(Long id);
	LpoModel getByNo(String no);
	boolean delete(Lpo lpo);
	List<LpoModel>getAllVisible();
	LpoDetailModel saveDetail(LpoDetail lpoDetail);
	LpoDetailModel getDetail(Long id);
	boolean deleteDetail(LpoDetail lpoDetail);
	List<LpoDetailModel>getAllDetails(Lpo lpo);	
}