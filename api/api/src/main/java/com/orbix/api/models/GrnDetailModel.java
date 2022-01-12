/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Grn;
import com.orbix.api.domain.Product;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class GrnDetailModel {
	Long id = null;
	double qtyOrdered;
	double qtyReceived = 0;
	double supplierPriceVatIncl = 0;
	double supplierPriceVatExcl = 0;
	double clientPriceVatIncl = 0;
	double clientPriceVatExcl = 0;
	Product product = null;
	Grn grn = null;
}
