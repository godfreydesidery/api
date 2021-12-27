/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Day;
import com.orbix.api.domain.Material;
import com.orbix.api.domain.MaterialStock;

/**
 * @author GODFREY
 *
 */
public interface MaterialStockService {
	boolean openStock(List<Material> materials, Day day);
	boolean closeStock(List<Material> materials, Day day);
	void saveStock(MaterialStock stock);
}
