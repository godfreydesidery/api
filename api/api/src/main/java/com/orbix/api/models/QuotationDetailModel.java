/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.Quotation;
import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class QuotationDetailModel {
	Long id = null;
	double qty = 0;
	double sellingPriceVatIncl = 0;
	double sellingPriceVatExcl = 0;
	Product product = null;
	Quotation quotation = null;
}
