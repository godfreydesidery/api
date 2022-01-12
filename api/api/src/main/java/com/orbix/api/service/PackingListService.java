/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.PackingList;
import com.orbix.api.domain.PackingListDetail;
import com.orbix.api.models.PackingListDetailModel;
import com.orbix.api.models.PackingListModel;

/**
 * @author GODFREY
 *
 */
public interface PackingListService {
	PackingListModel save(PackingList packingList);
	PackingListModel get(Long id);
	PackingListModel getByNo(String no);
	boolean delete(PackingList packingList);
	List<PackingListModel>getAllVisible();
	PackingListDetailModel saveDetail(PackingListDetail packingListDetail);
	PackingListDetailModel getDetail(Long id);
	boolean deleteDetail(PackingListDetail packingListDetail);
	List<PackingListDetailModel>getAllDetails(PackingList packingList);	
	boolean archive(PackingList packingList);
	boolean archiveAll();
	PackingListModel print(PackingList packingList);
	PackingListModel post(PackingList packingList, HttpServletRequest request);	
}
