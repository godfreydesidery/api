/**
 * 
 */
package com.orbix.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Day;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductStock;
import com.orbix.api.repositories.ProductStockRepository;
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
public class ProductStockServiceImpl implements ProductStockService {
	
	private final ProductStockRepository productStockRepository;

	@Override
	public boolean openStock(List<Product> products, Day day) {
		for(Product p : products) {
			ProductStock stock = new ProductStock();
			stock.setProduct(p);
			stock.setOpeningStock(p.getStock());
			stock.setCostPriceVatIncl(p.getCostPriceVatIncl());
			stock.setCostPriceVatExcl(p.getCostPriceVatExcl());
			stock.setSellingPriceVatIncl(p.getSellingPriceVatIncl());
			stock.setSellingPriceVatExcl(p.getSellingPriceVatExcl());
			stock.setDay(day);
			this.saveStock(stock);
		}
		return true;
	}

	@Override
	public boolean closeStock(List<Product> products, Day day) {
		for(Product p : products) {
			ProductStock stock = new ProductStock();			
			Optional<ProductStock> ps = productStockRepository.findByProductAndDay(p, day);
			if(ps.isPresent()) {
				stock = ps.get();
				stock.setClosingStock(p.getStock());
			}else {
				stock.setProduct(p);
				stock.setClosingStock(p.getStock());
				stock.setCostPriceVatIncl(p.getCostPriceVatIncl());
				stock.setCostPriceVatExcl(p.getCostPriceVatExcl());
				stock.setSellingPriceVatIncl(p.getSellingPriceVatIncl());
				stock.setSellingPriceVatExcl(p.getSellingPriceVatExcl());
				stock.setDay(day);
			}		
			this.saveStock(stock);
		}
		return true;
	}

	@Override
	public void saveStock(ProductStock stock) {
		productStockRepository.saveAndFlush(stock);
	}

}
