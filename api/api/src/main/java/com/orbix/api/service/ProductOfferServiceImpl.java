/**
 * 
 */
package com.orbix.api.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.ProductOffer;
import com.orbix.api.repositories.ProductOfferRepository;

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
public class ProductOfferServiceImpl implements ProductOfferService {

	private final ProductOfferRepository productOfferRepository;
	
	@Override
	public ProductOffer save(ProductOffer productOffer) {
		return productOfferRepository.saveAndFlush(productOffer);
	}

}
