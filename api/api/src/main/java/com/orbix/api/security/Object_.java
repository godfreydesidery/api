/**
 * 
 */
package com.orbix.api.security;

/**
 * @author GODFREY
 *
 */
public class Object_ {
	
	/**
	 * List of authorities not allowed
	 * CREATE READ UPDATE DELETE ACTIVATE APPROVE PRINT CANCEL
	 * 
	 * Format: OBJECT-LIST OF NOT ALLOWED AUTHORITIES
	 */
	
	public static String ADMIN = "ADMIN";
	public static String USER = "USER-APPROVE PRINT CANCEL";
	public static String ROLE = "ROLE-ACTIVATE APPROVE PRINT CANCEL";
	public static String TILL = "TILL-APPROVE PRINT CANCEL";
	public static String COMPANY_PROFILE = "COMPANY_PROFILE-DELETE ACTIVATE APPROVE PRINT CANCEL";
	public static String CUSTOMER = "CUSTOMER-APPROVE PRINT CANCEL";
	public static String EMPLOYEE = "EMPLOYEE-APPROVE PRINT CANCEL";
	public static String SUPPLIER = "SUPPLIER-APPROVE PRINT CANCEL";
	public static String PRODUCT = "PRODUCT-APPROVE PRINT CANCEL";
	public static String LPO = "LPO-ACTIVATE";
	public static String GRN = "GRN-ACTIVATE";
	public static String SALES_INVOICE = "SALES_INVOICE-ACTIVATE";
	public static String QUOTATION = "QUOTATION-ACTIVATE";
	public static String PACKING_LIST = "PACKING_LIST-ACTIVATE";
	public static String SALES_RECEIPT = "SALES_RECEIPT-ACTIVATE";
	public static String ALLOCATION = "ALLOCATION-UPDATE DELETE ACTIVATE APPROVE PRINT CANCEL";
	public static String DEBT_RECEIPT = "DEBT_RECEIPT-ACTIVATE";
	public static String MATERIAL = "MATERIAL-APPROVE PRINT CANCEL";
	
}
