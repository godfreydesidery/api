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
public class ProductionUnverifiedProductModel {
	public Long id = null;
	public double qty = 0;	
    public Production production = null;	
    public Product product = null;
}
