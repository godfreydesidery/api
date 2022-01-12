/**
 * 
 */
package com.orbix.api.service;

import java.time.LocalDate;
import java.util.List;

import com.orbix.api.domain.Material;
import com.orbix.api.domain.MaterialStockCard;

/**
 * @author GODFREY
 *
 */
public interface MaterialStockCardService {
	MaterialStockCard save(MaterialStockCard stockCard);
	List<MaterialStockCard>getAll();
	List<MaterialStockCard>getByMaterial(Material product);
	List<MaterialStockCard>getByDate(LocalDate startDate, LocalDate endDate);
}
