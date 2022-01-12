/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.Sale;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class SaleDetailModel {
	Long id = null;
	double qty = 0;
	double costPriceVatIncl = 0;
	double costPriceVatExcl = 0;
	double sellingPriceVatIncl = 0;
	double sellingPriceVatExcl = 0;
	double discount = 0;
	double tax = 0;
    Product product = null;
    Sale sale = null;
}
