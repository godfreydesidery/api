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
public interface PettyCashService {
	public void pettyCash(double amount, String details, Till till, HttpServletRequest request);
}
