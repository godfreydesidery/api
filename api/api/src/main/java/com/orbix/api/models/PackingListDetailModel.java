/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Product;
import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class PackingListDetailModel {
	Long id = null;	
	double previousReturns = 0;
	double qtyIssued = 0;
	double totalPacked = 0;
	double qtySold = 0;
	double qtyOffered = 0;
	double qtyReturned = 0;
	double qtyDamaged = 0;
	double costPriceVatIncl = 0;
	double costPriceVatExcl = 0;
	double sellingPriceVatIncl = 0;
	double sellingPriceVatExcl = 0;
	
    Product product = null;
}
