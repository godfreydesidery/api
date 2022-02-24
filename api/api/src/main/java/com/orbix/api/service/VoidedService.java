/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Till;

/**
 * @author GODFREY
 *
 */
public interface VoidedService {
	public void createVoid(Till till,String barcode, String code, String description, double qty, double sellingPriceVatIncl, HttpServletRequest request, Long createdAt, Long refId);
	public void deleteVoid(Long refId);
	public void checkVoid(Long refId);
}
