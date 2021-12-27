/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Lpo;
import com.orbix.api.domain.Product;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class LpoDetailModel {
	Long id = null;
	double qty = 0;
	double costPriceVatIncl = 0;
	double costPriceVatExcl = 0;
	Product product = null;
	Lpo lpo = null;
}
