/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Material;
import com.orbix.api.domain.Production;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class ProductionUnverifiedMaterialModel {
	Long id = null;
	double qty = 0;	
    Production production = null;		
    Material material = null;
}
