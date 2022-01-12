/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.Production;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class ProductionProductModel {
	Long id;
	double qty = 0;
	double costPriceVatIncl = 0;
	double costPriceVatExcl = 0;
	double sellingPriceVatIncl = 0;
	double sellingPriceVatExcl = 0;	
    Production production;	
    Product product;
}
