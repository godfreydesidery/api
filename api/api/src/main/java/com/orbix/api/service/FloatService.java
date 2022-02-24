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
public interface FloatService {
	public void addFloat(double amount, Till till, HttpServletRequest request);
	public void deductFloat(double amount, Till till, HttpServletRequest request);
}
