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
	double costPriceVatIncl = 0;
	double costPriceVatExcl = 0;
	Product product = null;
	Grn grn = null;
}
