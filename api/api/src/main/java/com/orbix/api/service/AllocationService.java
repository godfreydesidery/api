/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Customer;
import com.orbix.api.domain.SalesInvoice;

/**
 * @author GODFREY
 *
 */
public interface AllocationService {
	boolean allocate(Customer customer, SalesInvoice salesInvoice, HttpServletRequest request);
}
