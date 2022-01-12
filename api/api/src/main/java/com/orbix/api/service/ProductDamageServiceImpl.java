/**
 * 
 */
package com.orbix.api.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.ProductDamage;
import com.orbix.api.repositories.ProductDamageRepository;
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
public class ProductDamageServiceImpl implements ProductDamageService {

	private final ProductDamageRepository productDamageRepository;
	
	@Override
	public ProductDamage save(ProductDamage productDamage) {
		return productDamageRepository.saveAndFlush(productDamage);
	}

}
