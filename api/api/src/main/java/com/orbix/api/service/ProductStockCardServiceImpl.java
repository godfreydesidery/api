/**
 * 
 */
package com.orbix.api.service;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductStockCard;
import com.orbix.api.repositories.ProductStockCardRepository;

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
public class ProductStockCardServiceImpl implements ProductStockCardService {
	
	private final ProductStockCardRepository productStockCardRepository;

	@Override
	public ProductStockCard save(ProductStockCard productStockCard) {
		return productStockCardRepository.saveAndFlush(productStockCard);
	}

	@Override
	public List<ProductStockCard> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductStockCard> getByProduct(Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductStockCard> getByDate(LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
