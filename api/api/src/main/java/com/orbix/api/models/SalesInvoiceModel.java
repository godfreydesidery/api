/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDate;
import java.util.List;

import com.orbix.api.domain.Customer;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class SalesInvoiceModel {
	Long id = null;
	String no = "";
	String status = "";
	LocalDate invoiceDate = null;
	String comments = "";
	double balance = 0;
	Customer customer = null;
	String created = "";
	String approved = "";
	List<SalesInvoiceDetailModel> salesInvoiceDetails;	
}
