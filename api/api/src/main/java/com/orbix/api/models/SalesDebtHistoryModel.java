/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.SalesDebt;

/**
 * @author GODFREY
 *
 */
public class SalesDebtHistoryModel {
    Long id;	
	double cr = 0;
	double dr = 0;
	double balance = 0;	
	String reference;
	String created;		
    SalesDebt salesDebt = null;
}
