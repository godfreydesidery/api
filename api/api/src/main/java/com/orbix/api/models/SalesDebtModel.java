/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Employee;
import com.orbix.api.domain.SalesList;

/**
 * @author GODFREY
 *
 */
public class SalesDebtModel {
	Long id = null;
    String no = "";
    String status = "";
	double amount = 0;
	double balance = 0;	
    String created = "";
    Employee employee = null;
    SalesList salesList = null;
}
