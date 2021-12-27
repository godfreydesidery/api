/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Department;
import com.orbix.api.domain.LevelFour;
import com.orbix.api.domain.LevelOne;
import com.orbix.api.domain.LevelThree;
import com.orbix.api.domain.LevelTwo;
import com.orbix.api.domain.Category;
import com.orbix.api.domain.Class;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.SubCategory;
import com.orbix.api.domain.SubClass;
import com.orbix.api.domain.Supplier;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.MissingInformationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.CategoryRepository;
import com.orbix.api.repositories.ClassRepository;
import com.orbix.api.repositories.DepartmentRepository;
import com.orbix.api.repositories.LevelFourRepository;
import com.orbix.api.repositories.LevelOneRepository;
import com.orbix.api.repositories.LevelThreeRepository;
import com.orbix.api.repositories.LevelTwoRepository;
import com.orbix.api.repositories.ProductRepository;
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
public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository productRepository;
	private final SupplierRepository supplierRepository;
	private final DepartmentRepository departmentRepository;
	private final ClassRepository classRepository;
	private final SubClassRepository subClassRepository;
	private final CategoryRepository categoryRepository;
	private final SubCategoryRepository subCategoryRepository;
	private final LevelOneRepository levelOneRepository;
	private final LevelTwoRepository levelTwoRepository;
	private final LevelThreeRepository levelThreeRepository;
	private final LevelFourRepository levelFourRepository;

	@Override
	public Product save(Product product) {
		Optional<Supplier> s = supplierRepository.findByName(product.getSupplier().getName());
		if(!s.isPresent()) {
			throw new MissingInformationException("Could not save product, supplier missing");
		}else {
			supplierRepository.save(s.get());
			product.setSupplier(s.get());
		}
		Optional<Department> d = departmentRepository.findByName(product.getDepartment().getName());
		if(d.isPresent()) {
			departmentRepository.save(d.get());
			product.setDepartment(d.get());
			Optional<Class> c = classRepository.findByName(product.getClass_().getName());
			if(c.isPresent()) {
				classRepository.save(c.get());
				product.setClass_(c.get());
				Optional<SubClass> sb = subClassRepository.findByName(product.getSubClass().getName());
				if(sb.isPresent()) {
					subClassRepository.save(sb.get());
					product.setSubClass(sb.get());
				}else {
					product.setSubClass(null);
				}
			}else {
				product.setClass_(null);
				product.setSubClass(null);
			}
		}else {
			product.setDepartment(null);
			product.setClass_(null);
			product.setSubClass(null);
		}
		
		Optional<Category> c = categoryRepository.findByName(product.getCategory().getName());
		if(c.isPresent()) {
			categoryRepository.save(c.get());
			product.setCategory(c.get());;
			Optional<SubCategory> sc = subCategoryRepository.findByName(product.getSubCategory().getName());
			if(sc.isPresent()) {
				subCategoryRepository.save(sc.get());
				product.setSubCategory(sc.get());
			}else {
				product.setSubCategory(null);
			}
		}else {
			product.setCategory(null);
			product.setSubCategory(null);
		}
		
		Optional<LevelOne> one = levelOneRepository.findByName(product.getLevelOne().getName());
		if(one.isPresent()) {
			levelOneRepository.save(one.get());
			product.setLevelOne(one.get());
		}else {
			product.setLevelOne(null);
		}
		
		Optional<LevelTwo> two = levelTwoRepository.findByName(product.getLevelTwo().getName());
		if(two.isPresent()) {
			levelTwoRepository.save(two.get());
			product.setLevelTwo(two.get());
		}else {
			product.setLevelTwo(null);
		}
		
		Optional<LevelThree> three = levelThreeRepository.findByName(product.getLevelThree().getName());
		if(three.isPresent()) {
			levelThreeRepository.save(three.get());
			product.setLevelThree(three.get());
		}else {
			product.setLevelThree(null);
		}
		
		Optional<LevelFour> four = levelFourRepository.findByName(product.getLevelFour().getName());
		if(four.isPresent()) {
			levelFourRepository.save(four.get());
			product.setLevelFour(four.get());
		}else {
			product.setLevelFour(null);
		}
		
		if(validate(product)) {
			//Continue, else throw validation error
		}
		
		return productRepository.save(product);
		
	}

	@Override
	public Product get(Long id) {
		return productRepository.findById(id).get();
	}

	@Override
	public Product getByBarcode(String barcode) {
		Optional<Product> p = productRepository.findByBarcode(barcode);
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		return p.get();
	}

	@Override
	public Product getByCode(String code) {
		Optional<Product> p = productRepository.findByCode(code);
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		return p.get();
	}

	@Override
	public Product getByDescription(String description) {
		Optional<Product> p = productRepository.findByDescription(description);
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		return p.get();
	}

	@Override
	public Product getByCommonName(String commonName) {
		Optional<Product> p = productRepository.findByCommonName(commonName);
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		return p.get();
	}

	@Override
	public boolean delete(Product product) {
		if(!allowDelete(product)) {
			throw new InvalidOperationException("Deleting this product is not allowed");
		}
		productRepository.delete(product);
		return true;
	}

	@Override
	public List<Product> getAll() {
		return productRepository.findAll();
	}
	
	private boolean validate(Product product) {
		if(product.getCode().equals("")) {
			throw new MissingInformationException("Product Code required");
		}
		if(product.getDescription().equals("")) {
			throw new MissingInformationException("Product Description required");
		}
		if(product.getSupplier().getName().equals("")) {
			throw new MissingInformationException("Supplier information required");
		}
		if(product.getDiscount() < 0 || product.getDiscount() > 100) {
			throw new InvalidEntryException("Discount ratio should be between 0 and 100");
		}
		if(product.getVat() < 0 || product.getVat() > 100) {
			throw new InvalidEntryException("VAT ratio should be between 0 and 100");
		}
		if(product.getProfitMargin() < 0 ) {
			throw new InvalidEntryException("Profit margin should be more than 0");
		}
		return true;
	}
	
	private boolean allowDelete(Product product) {
		return true;
	}

	@Override
	public List<String> getSellableDescriptions() {
		return productRepository.getSellableProductDescriptions();	
	}
}
