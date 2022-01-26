/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Debt;
import com.orbix.api.domain.Employee;

/**
 * @author GODFREY
 *
 */
public interface DebtAllocationService {
	boolean allocate(Employee employee, Debt debt, HttpServletRequest request);
}
