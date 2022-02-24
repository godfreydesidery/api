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
public interface CashPickUpService {
	public void pickUp(double amount, Till till, HttpServletRequest request);
}
