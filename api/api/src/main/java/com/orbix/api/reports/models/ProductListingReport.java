/**
 * 
 */
package com.orbix.api.reports.models;

import java.time.LocalDate;

import com.orbix.api.domain.Product;

/**
 * @author GODFREY
 *
 */
public interface ProductListingReport {
	LocalDate getDate();
	Product getProduct();
	double getAmount();
	double getDiscount();
	double getTax();
}
