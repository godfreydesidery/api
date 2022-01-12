/**
 * 
 */
package com.orbix.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Category;
import com.orbix.api.domain.Material;
import com.orbix.api.domain.SubCategory;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.CategoryRepository;
import com.orbix.api.repositories.MaterialRepository;
import com.orbix.api.repositories.SubCategoryRepository;
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
public class MaterialServiceImpl implements MaterialService {
	
	private final MaterialRepository materialRepository;
	private final CategoryRepository categoryRepository;
	private final SubCategoryRepository subCategoryRepository;
	
	@Override
	public ArrayList<String> getNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Material save(Material material) {		
		Optional<Category> c = categoryRepository.findByName(material.getCategory().getName());
		if(c.isPresent()) {
			categoryRepository.save(c.get());
			material.setCategory(c.get());;
			Optional<SubCategory> sc = subCategoryRepository.findByName(material.getSubCategory().getName());
			if(sc.isPresent()) {
				subCategoryRepository.save(sc.get());
				material.setSubCategory(sc.get());
			}else {
				material.setSubCategory(null);
			}
		}else {
			material.setCategory(null);
			material.setSubCategory(null);
		}
		
		if(validate(material)) {
			//Continue, else throw validation error
		}	
		return materialRepository.save(material);		
	}

	@Override
	public Material get(Long id) {
		return materialRepository.findById(id).get();
	}

	@Override
	public Material getByCode(String code) {
		Optional<Material> m = materialRepository.findByCode(code);
		if(!m.isPresent()) {
			throw new NotFoundException("Material not found");
		}
		return m.get();
	}

	@Override
	public Material getByDescription(String description) {
		Optional<Material> m = materialRepository.findByDescription(description);
		if(!m.isPresent()) {
			throw new NotFoundException("Material not found");
		}
		return m.get();
	}

	@Override
	public boolean delete(Material material) {
		if(!allowDelete(material)) {
			throw new InvalidOperationException("Deleting this material is not allowed");
		}
		materialRepository.delete(material);
		return true;
	}

	@Override
	public List<Material> getAll() {
		return materialRepository.findAll();
	}
	
	private boolean validate(Material material) {		
		return true;
	}
	
	private boolean allowDelete(Material material) {
		return true;
	}
	
	@Override
	public List<String> getActiveDescriptions() {
		return materialRepository.getActiveDescriptions();	
	}

}
