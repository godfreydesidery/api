/**
 * 
 */
package com.orbix.api.reports.models;

import java.time.LocalDate;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
public interface DailySalesReport {
	LocalDate getDate();
	double getAmount();
	double getDiscount();
	double getTax();
}
