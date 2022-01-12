/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDate;

import com.orbix.api.domain.Customer;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class SalesReceiptModel {
	Long id = null;
	String no = "";
	String status = "";
	LocalDate receiptDate;
	String mode = "";
	double amount = 0;
	String chequeNo = "";	
	String comments = "";	
	String created = "";
	String approved = "";
    Customer customer = null;
}
