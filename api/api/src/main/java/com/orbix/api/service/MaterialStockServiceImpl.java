/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Day;
import com.orbix.api.domain.Material;
import com.orbix.api.domain.MaterialStock;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductStock;
import com.orbix.api.repositories.CategoryRepository;
import com.orbix.api.repositories.ClassRepository;
import com.orbix.api.repositories.DepartmentRepository;
import com.orbix.api.repositories.LevelFourRepository;
import com.orbix.api.repositories.LevelOneRepository;
import com.orbix.api.repositories.LevelThreeRepository;
import com.orbix.api.repositories.LevelTwoRepository;
import com.orbix.api.repositories.MaterialStockRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.ProductStockRepository;
import com.orbix.api.repositories.SubCategoryRepository;
import com.orbix.api.repositories.SubClassRepository;
import com.orbix.api.repositories.SupplierRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GODFREY
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MaterialStockServiceImpl implements MaterialStockService {

	private final MaterialStockRepository materialStockRepository;

	@Override
	public boolean openStock(List<Material> materials, Day day) {
		for(Material p : materials) {
			MaterialStock stock = new MaterialStock();
			stock.setMaterial(p);
			stock.setOpeningStock(p.getStock());
			stock.setCostPriceVatIncl(p.getCostPriceVatIncl());
			stock.setCostPriceVatExcl(p.getCostPriceVatExcl());
			stock.setDay(day);
			this.saveStock(stock);
		}
		return true;
	}

	@Override
	public boolean closeStock(List<Material> materials, Day day) {
		for(Material p : materials) {
			MaterialStock stock = new MaterialStock();			
			Optional<MaterialStock> ps = materialStockRepository.findByMaterialAndDay(p, day);
			if(ps.isPresent()) {
				stock = ps.get();
				stock.setClosingStock(p.getStock());
			}else {
				stock.setMaterial(p);
				stock.setClosingStock(p.getStock());
				stock.setCostPriceVatIncl(p.getCostPriceVatIncl());
				stock.setCostPriceVatExcl(p.getCostPriceVatExcl());
				stock.setDay(day);
			}		
			this.saveStock(stock);
		}
		return true;
	}

	@Override
	public void saveStock(MaterialStock stock) {
		materialStockRepository.saveAndFlush(stock);
	}
}
