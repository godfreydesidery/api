/**
 * 
 */
package com.orbix.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Material;
import com.orbix.api.domain.MaterialStockCard;
import com.orbix.api.domain.PackingList;
import com.orbix.api.domain.PackingListDetail;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductStockCard;
import com.orbix.api.domain.Production;
import com.orbix.api.domain.ProductionMaterial;
import com.orbix.api.domain.ProductionProduct;
import com.orbix.api.domain.ProductionUnverifiedMaterial;
import com.orbix.api.domain.ProductionUnverifiedProduct;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.PackingListDetailModel;
import com.orbix.api.models.PackingListModel;
import com.orbix.api.models.ProductionMaterialModel;
import com.orbix.api.models.ProductionModel;
import com.orbix.api.models.ProductionProductModel;
import com.orbix.api.models.ProductionUnverifiedMaterialModel;
import com.orbix.api.models.ProductionUnverifiedProductModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.MaterialRepository;
import com.orbix.api.repositories.MaterialStockCardRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.ProductStockCardRepository;
import com.orbix.api.repositories.ProductionMaterialRepository;
import com.orbix.api.repositories.ProductionProductRepository;
import com.orbix.api.repositories.ProductionRepository;
import com.orbix.api.repositories.ProductionUnverifiedMaterialRepository;
import com.orbix.api.repositories.ProductionUnverifiedProductRepository;
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
public class ProductionServiceImpl implements ProductionService {
	
	private final ProductionRepository productionRepository;
	private final ProductionProductRepository productionProductRepository;
	private final ProductionUnverifiedProductRepository productionUnverifiedProductRepository;
	private final ProductionMaterialRepository productionMaterialRepository;
	private final ProductionUnverifiedMaterialRepository productionUnverifiedMaterialRepository;
	
	private final MaterialRepository materialRepository;
	private final ProductRepository productRepository;
	
	private final MaterialStockCardRepository materialStockCardRepository;
	private final ProductStockCardRepository productStockCardRepository;
	
	private final DayRepository dayRepository;
	private final UserRepository userRepository;
	
	private final MaterialStockCardService materialStockCardService;
	private final ProductStockCardService productStockCardService;
	
	@Override
	public ProductionModel get(Long id) {
		ProductionModel model = new ProductionModel();
		Optional<Production> prod = productionRepository.findById(id);
		if(!prod.isPresent()) {
			throw new NotFoundException("Production not found");
		}
		model.setId(prod.get().getId());
		model.setNo(prod.get().getNo());
		model.setProductionName(prod.get().getProductionName());
		model.setBatchNo(prod.get().getBatchNo());
		model.setBatchSize(prod.get().getBatchSize());
		model.setStatus(prod.get().getStatus());
		model.setComments(prod.get().getComments());
		model.setUom(prod.get().getUom());
		
		if(prod.get().getCreatedAt() != null && prod.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(prod.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.get().getCreatedBy()));
		}
		if(prod.get().getOpenedAt() != null && prod.get().getOpenedBy() != null) {
			model.setOpened(dayRepository.findById(prod.get().getOpenedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.get().getOpenedBy()));
		}
		if(prod.get().getClosedAt() != null && prod.get().getClosedBy() != null) {
			model.setClosed(dayRepository.findById(prod.get().getClosedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.get().getClosedBy()));
		}
		List<ProductionProduct> productionProducts = prod.get().getProductionProducts();
		List<ProductionUnverifiedProduct> productionUnverifiedProducts = prod.get().getProductionUnverifiedProducts();
		List<ProductionMaterial> productionMaterials = prod.get().getProductionMaterials();
		List<ProductionUnverifiedMaterial> productionUnverifiedMaterials = prod.get().getProductionUnverifiedMaterials();
		
		
		List<ProductionProductModel> productionProductModels = new ArrayList<ProductionProductModel>();
		List<ProductionUnverifiedProductModel> productionUnverifiedProductModels = new ArrayList<ProductionUnverifiedProductModel>();
		List<ProductionMaterialModel> productionMaterialModels = new ArrayList<ProductionMaterialModel>();
		List<ProductionUnverifiedMaterialModel> productionUnverifiedMaterialModels = new ArrayList<ProductionUnverifiedMaterialModel>();
		
		for(ProductionProduct pp : productionProducts) {
			ProductionProductModel pm = new ProductionProductModel();
			pm.setId(pp.getId());
			pm.setCostPriceVatIncl(pp.getCostPriceVatIncl());
			pm.setCostPriceVatExcl(pp.getCostPriceVatExcl());
			pm.setSellingPriceVatIncl(pp.getSellingPriceVatIncl());
			pm.setSellingPriceVatExcl(pp.getSellingPriceVatExcl());
			pm.setProduct(pp.getProduct());
			pm.setProduction(prod.get());
			pm.setQty(pp.getQty());
			productionProductModels.add(pm);
		}
		model.setProductionProducts(productionProductModels);
		
		for(ProductionUnverifiedProduct pp : productionUnverifiedProducts) {
			ProductionUnverifiedProductModel pm = new ProductionUnverifiedProductModel();
			pm.setId(pp.getId());
			pm.setProduct(pp.getProduct());
			pm.setProduction(prod.get());
			pm.setQty(pp.getQty());
			productionUnverifiedProductModels.add(pm);
		}
		model.setProductionUnverifiedProducts(productionUnverifiedProductModels);
		
		for(ProductionMaterial pp : productionMaterials) {
			ProductionMaterialModel pm = new ProductionMaterialModel();
			pm.setId(pp.getId());			
			pm.setMaterial(pp.getMaterial());
			pm.setProduction(prod.get());
			pm.setQty(pp.getQty());
			productionMaterialModels.add(pm);
		}
		model.setProductionMaterials(productionMaterialModels);
		
		for(ProductionUnverifiedMaterial pp : productionUnverifiedMaterials) {
			ProductionUnverifiedMaterialModel pm = new ProductionUnverifiedMaterialModel();
			pm.setId(pp.getId());			
			pm.setMaterial(pp.getMaterial());
			pm.setProduction(prod.get());
			pm.setQty(pp.getQty());
			productionUnverifiedMaterialModels.add(pm);
		}
		model.setProductionUnverifiedMaterials(productionUnverifiedMaterialModels);
		
		return model;
	}
	
	@Override
	public ProductionModel getByNo(String no) {
		ProductionModel model = new ProductionModel();
		Optional<Production> prod = productionRepository.findByNo(no);
		if(!prod.isPresent()) {
			throw new NotFoundException("Production not found");
		}
		model.setId(prod.get().getId());
		model.setNo(prod.get().getNo());
		model.setProductionName(prod.get().getProductionName());
		model.setBatchNo(prod.get().getBatchNo());
		model.setBatchSize(prod.get().getBatchSize());
		model.setStatus(prod.get().getStatus());
		model.setComments(prod.get().getComments());
		model.setUom(prod.get().getComments());
		
		if(prod.get().getCreatedAt() != null && prod.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(prod.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.get().getCreatedBy()));
		}
		if(prod.get().getOpenedAt() != null && prod.get().getOpenedBy() != null) {
			model.setOpened(dayRepository.findById(prod.get().getOpenedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.get().getOpenedBy()));
		}
		if(prod.get().getClosedAt() != null && prod.get().getClosedBy() != null) {
			model.setClosed(dayRepository.findById(prod.get().getClosedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.get().getClosedBy()));
		}
		List<ProductionProduct> productionProducts = prod.get().getProductionProducts();
		List<ProductionUnverifiedProduct> productionUnverifiedProducts = prod.get().getProductionUnverifiedProducts();
		List<ProductionMaterial> productionMaterials = prod.get().getProductionMaterials();
		List<ProductionUnverifiedMaterial> productionUnverifiedMaterials = prod.get().getProductionUnverifiedMaterials();
		
		
		List<ProductionProductModel> productionProductModels = new ArrayList<ProductionProductModel>();
		List<ProductionUnverifiedProductModel> productionUnverifiedProductModels = new ArrayList<ProductionUnverifiedProductModel>();
		List<ProductionMaterialModel> productionMaterialModels = new ArrayList<ProductionMaterialModel>();
		List<ProductionUnverifiedMaterialModel> productionUnverifiedMaterialModels = new ArrayList<ProductionUnverifiedMaterialModel>();
		
		for(ProductionProduct pp : productionProducts) {
			ProductionProductModel pm = new ProductionProductModel();
			pm.setId(pp.getId());
			pm.setCostPriceVatIncl(pp.getCostPriceVatIncl());
			pm.setCostPriceVatExcl(pp.getCostPriceVatExcl());
			pm.setSellingPriceVatIncl(pp.getSellingPriceVatIncl());
			pm.setSellingPriceVatExcl(pp.getSellingPriceVatExcl());
			pm.setProduct(pp.getProduct());
			pm.setQty(pp.getQty());
			productionProductModels.add(pm);
		}
		model.setProductionProducts(productionProductModels);
		
		for(ProductionUnverifiedProduct pp : productionUnverifiedProducts) {
			ProductionUnverifiedProductModel pm = new ProductionUnverifiedProductModel();
			pm.setId(pp.getId());
			pm.setProduct(pp.getProduct());
			pm.setQty(pp.getQty());
			productionUnverifiedProductModels.add(pm);
		}
		model.setProductionUnverifiedProducts(productionUnverifiedProductModels);
		
		for(ProductionMaterial pp : productionMaterials) {
			ProductionMaterialModel pm = new ProductionMaterialModel();
			pm.setId(pp.getId());			
			pm.setMaterial(pp.getMaterial());
			pm.setQty(pp.getQty());
			productionMaterialModels.add(pm);
		}
		model.setProductionMaterials(productionMaterialModels);
		
		for(ProductionUnverifiedMaterial pp : productionUnverifiedMaterials) {
			ProductionUnverifiedMaterialModel pm = new ProductionUnverifiedMaterialModel();
			pm.setId(pp.getId());			
			pm.setMaterial(pp.getMaterial());
			pm.setQty(pp.getQty());
			productionUnverifiedMaterialModels.add(pm);
		}
		model.setProductionUnverifiedMaterials(productionUnverifiedMaterialModels);
		
		return model;
	}
	
	@Override
	public ProductionModel save(Production production) {
		if(!validate(production)) {
			throw new InvalidEntryException("Could not save production");
		}
		Production prod = productionRepository.save(production);
		if(prod.getNo().equals("NA")) {
			prod.setNo(generateProductionNo(prod));
			prod = productionRepository.save(prod);
		}			
		ProductionModel model = new ProductionModel();
		model.setId(prod.getId());
		model.setNo(prod.getNo());
		model.setProductionName(prod.getProductionName());
		model.setBatchNo(prod.getBatchNo());
		model.setBatchSize(prod.getBatchSize());
		model.setUom(prod.getUom());
		model.setStatus(prod.getStatus());
		model.setComments(prod.getComments());
		
		if(prod.getCreatedAt() != null && prod.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(prod.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.getCreatedBy()));
		}
		if(prod.getOpenedAt() != null && prod.getOpenedBy() != null) {
			model.setOpened(dayRepository.findById(prod.getOpenedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.getOpenedBy()));
		}
		if(prod.getClosedAt() != null && prod.getClosedBy() != null) {
			model.setClosed(dayRepository.findById(prod.getClosedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.getClosedBy()));
		}		
		return model;
	}

	@Override
	public ProductionModel close(Production production) {
		if(!production.getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not process, only OPEN production can be  closed");
		}
		List<ProductionUnverifiedMaterial> pum = productionUnverifiedMaterialRepository.findByProduction(production);
		if(!pum.isEmpty()) {
			throw new InvalidOperationException("The production could not be closed, there are still unverified materials, please remove or verify unverified materials.");
		}
		List<ProductionUnverifiedProduct> pup = productionUnverifiedProductRepository.findByProduction(production);
		if(!pup.isEmpty()) {
			throw new InvalidOperationException("The production could not be closed, there are still unverified products, please remove or verify unverified products.");
		}
		List<ProductionMaterial> pm = productionMaterialRepository.findByProduction(production);
		if(pm.isEmpty()) {
			throw new InvalidOperationException("The production could not be closed, no materials have been used, please verify the unverified materials.");
		}
		List<ProductionProduct> pp = productionProductRepository.findByProduction(production);
		if(pp.isEmpty()) {
			throw new InvalidOperationException("The production could not be closed, no products have been produced, please verify the unverified products.");
		}		
		production.setStatus("CLOSED");
		Production prod = productionRepository.saveAndFlush(production);
		ProductionModel model = new ProductionModel();
		model.setId(prod.getId());
		model.setNo(prod.getNo());
		model.setProductionName(prod.getProductionName());
		model.setBatchNo(prod.getBatchNo());
		model.setBatchSize(prod.getBatchSize());
		model.setUom(prod.getUom());
		model.setStatus(prod.getStatus());
		model.setComments(prod.getComments());
		
		if(prod.getCreatedAt() != null && prod.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(prod.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.getCreatedBy()));
		}
		if(prod.getOpenedAt() != null && prod.getOpenedBy() != null) {
			model.setOpened(dayRepository.findById(prod.getOpenedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.getOpenedBy()));
		}
		if(prod.getClosedAt() != null && prod.getClosedBy() != null) {
			model.setClosed(dayRepository.findById(prod.getClosedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.getClosedBy()));
		}		
		return model;
	}

	@Override
	public ProductionModel cancel(Production production) {
		if(!production.getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not process, only OPEN production can be  canceled");
		}
		
		List<ProductionMaterial> materials = production.getProductionMaterials();
		if(!materials.isEmpty()) {
			throw new InvalidOperationException("The production could not be canceled. The production process has already consumed materials");
		}
		
		List<ProductionProduct> products = production.getProductionProducts();
		if(!products.isEmpty()) {
			throw new InvalidOperationException("The production could not be closed. The production process has already produced products");
		}
		
		List<ProductionUnverifiedMaterial> unverifiedMaterials = production.getProductionUnverifiedMaterials();
		if(!unverifiedMaterials.isEmpty()) {
			throw new InvalidOperationException("The production process can not be canceled. Please remove materials from material list");
		}
		
		List<ProductionUnverifiedProduct> unverifiedProducts = production.getProductionUnverifiedProducts();
		if(!unverifiedProducts.isEmpty()) {
			throw new InvalidOperationException("The production process can not be canceled. Please remove products from product list");
		}
		
		production.setStatus("CANCELED");
		Production prod = productionRepository.saveAndFlush(production);
		ProductionModel model = new ProductionModel();
		model.setId(prod.getId());
		model.setNo(prod.getNo());
		model.setProductionName(prod.getProductionName());
		model.setBatchNo(prod.getBatchNo());
		model.setBatchSize(prod.getBatchSize());
		model.setUom(prod.getUom());
		model.setStatus(prod.getStatus());
		model.setComments(prod.getComments());
		
		if(prod.getCreatedAt() != null && prod.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(prod.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.getCreatedBy()));
		}
		if(prod.getOpenedAt() != null && prod.getOpenedBy() != null) {
			model.setOpened(dayRepository.findById(prod.getOpenedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.getOpenedBy()));
		}
		if(prod.getClosedAt() != null && prod.getClosedBy() != null) {
			model.setClosed(dayRepository.findById(prod.getClosedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(prod.getClosedBy()));
		}		
		return model;
	}

	@Override
	public ProductionUnverifiedMaterialModel addMaterial(Production production, Material material, double qty) {
		if(!production.getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not add material, production is not OPEN");
		}
		if(qty <= 0) {
			throw new InvalidEntryException("Could not add material, invalid quantity value");
		}
		Optional<ProductionUnverifiedMaterial> mat = productionUnverifiedMaterialRepository.findByProductionAndMaterial(production, material);
		ProductionUnverifiedMaterialModel model = new ProductionUnverifiedMaterialModel();
		ProductionUnverifiedMaterial p = new ProductionUnverifiedMaterial();
		if(mat.isPresent()) {
			if(mat.get().getId() == material.getId()) {
				mat.get().setQty(mat.get().getQty() + qty);
				p = productionUnverifiedMaterialRepository.saveAndFlush(mat.get());
			}
		}else {
			ProductionUnverifiedMaterial pum = new ProductionUnverifiedMaterial();
			pum.setMaterial(material);
			pum.setProduction(production);
			pum.setProduction(production);
			p = productionUnverifiedMaterialRepository.saveAndFlush(pum);
		}
		model.setId(p.getId());
		model.setMaterial(material);
		model.setQty(p.getQty());
		return model;
	}

	@Override
	public ProductionUnverifiedMaterialModel deductMaterial(Production production, Material material, double qty) {
		if(!production.getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not add material, production is not OPEN");
		}
		if(qty <= 0) {
			throw new InvalidEntryException("Could not add material, invalid quantity value");
		}
		Optional<ProductionUnverifiedMaterial> mat = productionUnverifiedMaterialRepository.findByProductionAndMaterial(production, material);
		ProductionUnverifiedMaterialModel model = new ProductionUnverifiedMaterialModel();
		ProductionUnverifiedMaterial p = new ProductionUnverifiedMaterial();
		if(mat.isPresent()) {
			if(mat.get().getId() == material.getId()) {
				if(mat.get().getQty() < qty) {
					throw new InvalidOperationException("Could not deduct, quantity exceeds the available quantity");
				}
				mat.get().setQty(mat.get().getQty() - qty);
				p = productionUnverifiedMaterialRepository.saveAndFlush(mat.get());
			}
		}else {
			throw new NotFoundException("Material not available in material list");
		}
		model.setId(p.getId());
		model.setMaterial(material);
		model.setProduction(p.getProduction());
		model.setQty(p.getQty());
		return model;
	}

	@Override
	public ProductionMaterialModel verifyMaterial(Production production, Material material, double qty) {
		/**
		 * Checks if production is valid for verifying material
		 */
		if(!production.getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not verify material, production is not OPEN");
		}
		/**
		 * Validate quantity
		 */
		if(qty <= 0) {
			throw new InvalidEntryException("Could not verify material, invalid quantity value");
		}
		Optional<ProductionUnverifiedMaterial> pum = productionUnverifiedMaterialRepository.findByProductionAndMaterial(production, material);
		List<ProductionMaterial> materials = productionMaterialRepository.findByProduction(production);
		ProductionMaterial p = new ProductionMaterial();
		if(pum.isPresent()) {
			/**
			 * Checks whether material is present in verified list, if it is present
			 * Remove material from unverified list
			 * Add material to verified list
			 * Update material stock
			 * Update material stock cards
			 */
			for(ProductionMaterial pm : materials) {
				if(pum.get().getId() == pm.getId()) {
					productionUnverifiedMaterialRepository.delete(pum.get());					
					pm.setQty(pm.getQty() + qty);					
					p = productionMaterialRepository.saveAndFlush(pm);
				}
			}
		}else {
			ProductionMaterial pm = new ProductionMaterial();
			pm.setMaterial(material);
			pm.setProduction(production);
			pm.setProduction(production);
			p = productionMaterialRepository.saveAndFlush(pm);

		}
		/**
		 * Create an updated stock
		 */
		double stock = materialRepository.findById(pum.get().getId()).get().getStock() - qty;
		/**
		 * Update material stock
		 */
		Material m = new Material();
		m.setStock(stock);
		materialRepository.saveAndFlush(m);
		/**
		 * Now, update stock cards
		 */
		MaterialStockCard stockCard = new MaterialStockCard();
		
		stockCard.setQtyOut(qty);
		stockCard.setMaterial(material);
		stockCard.setBalance(stock);
		stockCard.setDay(dayRepository.getCurrentBussinessDay());
		stockCard.setReference("Used in production. Ref #: "+production.getNo());
		materialStockCardService.save(stockCard);
		
		ProductionMaterialModel model = new ProductionMaterialModel();
		model.setId(p.getId());
		model.setMaterial(p.getMaterial());
		model.setProduction(p.getProduction());
		model.setQty(p.getQty());
		
		return model;
	}

	@Override
	public boolean removeMaterial(Production production, Material material) {
		Optional<ProductionUnverifiedMaterial> mat = productionUnverifiedMaterialRepository.findByProductionAndMaterial(production, material);
		if(mat.isPresent()) {
			productionUnverifiedMaterialRepository.delete(mat.get());
			return true;
		}
		return false;
	}

	@Override
	public ProductionUnverifiedProductModel addProduct(Production production, Product product, double qty) {
		if(!production.getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not add product, production is not OPEN");
		}
		if(qty <= 0) {
			throw new InvalidEntryException("Could not add product, invalid quantity value");
		}
		Optional<ProductionUnverifiedProduct> prod = productionUnverifiedProductRepository.findByProductionAndProduct(production, product);
		ProductionUnverifiedProductModel model = new ProductionUnverifiedProductModel();
		ProductionUnverifiedProduct p = new ProductionUnverifiedProduct();
		if(prod.isPresent()) {
			if(prod.get().getId() == product.getId()) {
				prod.get().setQty(prod.get().getQty() + qty);
				p = productionUnverifiedProductRepository.saveAndFlush(prod.get());
			}
		}else {
			ProductionUnverifiedProduct pum = new ProductionUnverifiedProduct();
			pum.setProduct(product);
			pum.setProduction(production);
			pum.setProduction(production);
			p = productionUnverifiedProductRepository.saveAndFlush(pum);
		}
		model.setId(p.getId());
		model.setProduct(product);
		model.setQty(p.getQty());
		return model;
	}

	@Override
	public ProductionUnverifiedProductModel deductProduct(Production production, Product product, double qty) {
		if(!production.getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not add product, production is not OPEN");
		}
		if(qty <= 0) {
			throw new InvalidEntryException("Could not add product, invalid quantity value");
		}
		Optional<ProductionUnverifiedProduct> prod = productionUnverifiedProductRepository.findByProductionAndProduct(production, product);
		ProductionUnverifiedProductModel model = new ProductionUnverifiedProductModel();
		ProductionUnverifiedProduct p = new ProductionUnverifiedProduct();
		if(prod.isPresent()) {
			if(prod.get().getId() == product.getId()) {
				if(prod.get().getQty() < qty) {
					throw new InvalidOperationException("Could not deduct, quantity exceeds the available quantity");
				}
				prod.get().setQty(prod.get().getQty() - qty);
				p = productionUnverifiedProductRepository.saveAndFlush(prod.get());
			}
		}else {
			throw new NotFoundException("Product not available in product list");
		}
		model.setId(p.getId());
		model.setProduct(product);
		model.setProduction(p.getProduction());
		model.setQty(p.getQty());
		return model;
	}

	@Override
	public ProductionProductModel verifyProduct(Production production, Product product, double qty) {
		/**
		 * Checks if production is valid for verifying product
		 */
		if(!production.getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not verify product, production is not OPEN");
		}
		/**
		 * Validate quantity
		 */
		if(qty <= 0) {
			throw new InvalidEntryException("Could not verify product, invalid quantity value");
		}
		Optional<ProductionUnverifiedProduct> pum = productionUnverifiedProductRepository.findByProductionAndProduct(production, product);
		List<ProductionProduct> products = productionProductRepository.findByProduction(production);
		ProductionProduct p = new ProductionProduct();
		if(pum.isPresent()) {
			/**
			 * Checks whether product is present in verified list, if it is present
			 * Remove product from unverified list
			 * Add product to verified list
			 * Update product stock
			 * Update product stock cards
			 */
			for(ProductionProduct pm : products) {
				if(pum.get().getId() == pm.getId()) {
					productionUnverifiedProductRepository.delete(pum.get());					
					pm.setQty(pm.getQty() + qty);					
					p = productionProductRepository.saveAndFlush(pm);
				}
			}
		}else {
			ProductionProduct pm = new ProductionProduct();
			pm.setProduct(product);
			pm.setProduction(production);
			pm.setProduction(production);
			p = productionProductRepository.saveAndFlush(pm);

		}
		/**
		 * Create an updated stock
		 */
		double stock = productRepository.findById(pum.get().getId()).get().getStock() - qty;
		/**
		 * Update product stock
		 */
		Product m = new Product();
		m.setStock(stock);
		productRepository.saveAndFlush(m);
		/**
		 * Now, update stock cards
		 */
		ProductStockCard stockCard = new ProductStockCard();
		
		stockCard.setQtyOut(qty);
		stockCard.setProduct(product);
		stockCard.setBalance(stock);
		stockCard.setDay(dayRepository.getCurrentBussinessDay());
		stockCard.setReference("Used in production. Ref #: "+production.getNo());
		productStockCardService.save(stockCard);
		
		ProductionProductModel model = new ProductionProductModel();
		model.setId(p.getId());
		model.setProduct(p.getProduct());
		model.setProduction(p.getProduction());
		model.setQty(p.getQty());
		model.setCostPriceVatIncl(p.getCostPriceVatIncl());
		model.setCostPriceVatExcl(p.getCostPriceVatExcl());
		model.setSellingPriceVatIncl(p.getSellingPriceVatIncl());
		model.setSellingPriceVatExcl(p.getSellingPriceVatExcl());
		
		return model;
	}

	@Override
	public boolean removeProduct(Production production, Product product) {
		Optional<ProductionUnverifiedProduct> prod = productionUnverifiedProductRepository.findByProductionAndProduct(production, product);
		if(prod.isPresent()) {
			productionUnverifiedProductRepository.delete(prod.get());
			return true;
		}
		return false;
	}
	
	public boolean archiveAll() {
		// TODO Auto-generated method stub
		return false;
	}	
	
	private boolean validate(Production production) {
		if(production.getProductionName().equals("")) {
			throw new InvalidEntryException("Production name is required");
		}
		if(production.getBatchSize() <= 0) {
			throw new InvalidEntryException("Batch size must be positive");
		}
		if(production.getUom().equals("")) {
			throw new InvalidEntryException("Unit of measure is required");
		}
		return true;
	}
	
	private boolean allowDelete(Production production) {
		return true;
	}
	
	
	private String generateProductionNo(Production production) {
		Long number = production.getId();		
		String sNumber = number.toString();
		return "PRD-"+Formater.formatSix(sNumber);
	}
	
}
