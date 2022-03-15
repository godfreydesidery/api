/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDate;
import java.util.List;

import com.orbix.api.domain.Customer;
import com.orbix.api.domain.Employee;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class SalesListModel {
	Long id = null;
	String no = "";
	String status = "";
	LocalDate issueDate = null;
	String comments = "";
	String created = "";
	String approved = "";
	String posted = "";
	Customer customer = null;
	Employee employee = null;
	
	double totalPreviousReturns = 0;
	double totalAmountIssued = 0;
	double totalAmountPacked = 0;
	double totalSales = 0;
	double totalOffered = 0;
	double totalReturns = 0;
	double totalDamages = 0;
	
	double totalDiscounts = 0;
	double totalExpenditures = 0;
	double totalBank = 0;
	double totalCash = 0;
	double totalDeficit = 0;
	
	List<SalesListDetailModel> salesListDetails;
}
