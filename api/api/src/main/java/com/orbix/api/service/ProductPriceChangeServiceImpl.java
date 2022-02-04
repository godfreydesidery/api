/**
 * 
 */
package com.orbix.api.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.ProductPriceChange;
import com.orbix.api.repositories.ProductOfferRepository;
import com.orbix.api.repositories.ProductPriceChangeRepository;

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
public class ProductPriceChangeServiceImpl implements ProductPriceChangeService {
	
	private final ProductPriceChangeRepository productPriceChangeRepository;

	@Override
	public ProductPriceChange save(ProductPriceChange productPriceChange) {
		return productPriceChangeRepository.saveAndFlush(productPriceChange);
	}

}
