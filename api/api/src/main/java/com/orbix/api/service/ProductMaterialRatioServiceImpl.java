/**
 * 
 */
package com.orbix.api.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Material;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductMaterialRatio;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.ProductMaterialRatioModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.MaterialRepository;
import com.orbix.api.repositories.ProductMaterialRatioRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.UserRepository;

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
public class ProductMaterialRatioServiceImpl implements ProductMaterialRatioService {
	
	private final ProductRepository productRepository;
	private final MaterialRepository materialRepository;
	private final ProductMaterialRatioRepository productMaterialRatioRepository;
	private final DayRepository dayRepository;
	private final UserRepository userRepository;
	
	@Override
	public ProductMaterialRatioModel save(ProductMaterialRatio productMaterialRatio) {
		Optional<Product> prod = productRepository.findById(productMaterialRatio.getProduct().getId());
		if(!prod.isPresent()) {
			throw new InvalidOperationException("Product not found");
		}
		Optional<Material> mat = materialRepository.findById(productMaterialRatio.getProduct().getId());
		if(!mat.isPresent()) {
			throw new InvalidOperationException("Material not found");
		}
		Optional<ProductMaterialRatio> pr = productMaterialRatioRepository.findByProductAndMaterial(prod.get(), mat.get());
		ProductMaterialRatio pmr = new ProductMaterialRatio();
		if(pr.isPresent()) {
			pr.get().setRatio(productMaterialRatio.getRatio());
			pmr = productMaterialRatioRepository.saveAndFlush(pr.get());
		}else {
			pmr.setProduct(prod.get());
			pmr.setMaterial(mat.get());
			pmr.setRatio(productMaterialRatio.getRatio());
			pmr.setCreatedBy(productMaterialRatio.getCreatedBy());
			pmr.setCreatedAt(productMaterialRatio.getCreatedAt());
			pmr = productMaterialRatioRepository.saveAndFlush(pmr);
		}
		
		
		ProductMaterialRatioModel model = new ProductMaterialRatioModel();
		model.setId(pmr.getId());
		model.setProduct(pmr.getProduct());
		model.setMaterial(pmr.getMaterial());
		model.setRatio(pmr.getRatio());
		if(pmr.getCreatedAt() != null && pmr.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(pmr.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pmr.getCreatedBy()));
		}
		return model;
	}

	@Override
	public ProductMaterialRatioModel get(Long id) {
		ProductMaterialRatioModel model = new ProductMaterialRatioModel();
		Optional<ProductMaterialRatio> pmr = productMaterialRatioRepository.findById(id);
		if(!pmr.isPresent()) {
			throw new NotFoundException("Ratio not found");
		}
		model.setId(pmr.get().getId());
		model.setProduct(pmr.get().getProduct());
		model.setMaterial(pmr.get().getMaterial());
		model.setRatio(pmr.get().getRatio());
		
		
		if(pmr.get().getCreatedAt() != null && pmr.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(pmr.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pmr.get().getCreatedBy()));
		}	
		return model;
	}

	@Override
	public boolean delete(Long id) {
		Optional<ProductMaterialRatio> pmr = productMaterialRatioRepository.findById(id);
		if(!pmr.isPresent()) {
			throw new NotFoundException("Ratio not found");
		}
		productMaterialRatioRepository.delete(pmr.get());		
		return true;
	}

	@Override
	public ProductMaterialRatioModel getByProduct(Product product) {
		ProductMaterialRatioModel model = new ProductMaterialRatioModel();
		Optional<ProductMaterialRatio> pmr = productMaterialRatioRepository.findByProduct(product);
		if(!pmr.isPresent()) {
			throw new NotFoundException("Ratio not found");
		}
		model.setId(pmr.get().getId());
		model.setProduct(pmr.get().getProduct());
		model.setMaterial(pmr.get().getMaterial());
		model.setRatio(pmr.get().getRatio());
		
		
		if(pmr.get().getCreatedAt() != null && pmr.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(pmr.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pmr.get().getCreatedBy()));
		}	
		return model;
	}
	
}
