/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Debt;
import com.orbix.api.domain.Employee;
import com.orbix.api.domain.SalesDebt;
import com.orbix.api.models.DebtModel;

/**
 * @author GODFREY
 *
 */
public interface SalesDebtService {
	public SalesDebt create(SalesDebt salesDebt);
	public SalesDebt pay(SalesDebt salesDebt, double amount);
	public boolean transfer(SalesDebt fromSalesDebt, SalesDebt toSalesDebt, double amount);	
}
