/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.SalesInvoice;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class SalesInvoiceDetailModel {
	Long id = null;
	double qty = 0;
	double sellingPriceVatIncl = 0;
	double sellingPriceVatExcl = 0;
	Product product = null;
	SalesInvoice salesInvoice = null;
}
