/**
 * 
 */
package com.orbix.api.service;

import com.orbix.api.domain.Debt;

/**
 * @author GODFREY
 *
 */
public interface DebtService {
	public Debt create(Debt debt);
	public Debt pay(Debt debt, double amount);
}
