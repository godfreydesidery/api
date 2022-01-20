/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.domain.Material;
import com.orbix.api.domain.MaterialStockCard;
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
import com.orbix.api.models.PackingListModel;
import com.orbix.api.models.ProductionModel;
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
import com.orbix.api.service.ProductService;
import com.orbix.api.service.ProductionService;
import com.orbix.api.service.UserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductionResource {
	
	private final ProductionUnverifiedMaterialRepository productionUnverifiedMaterialRepository;
	private final ProductionUnverifiedProductRepository productionUnverifiedProductRepository;
	private final ProductionProductRepository productionProductRepository;
	private final ProductionMaterialRepository productionMaterialRepository;
	private final ProductionService productionService;
	private final ProductionRepository productionRepository;
	private final DayRepository dayRepository;
	private final UserService userService;
	private final MaterialRepository materialRepository;
	private final ProductRepository productRepository;
	private final MaterialStockCardRepository materialStockCardRepository;
	private final ProductStockCardRepository productStockCardRepository;

	@GetMapping("/productions")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-READ')")
	public ResponseEntity<List<Production>>getProductions(){
		return ResponseEntity.ok().body(productionRepository.findAll());
	}
	
	@GetMapping("/productions/get")
	//@PreAuthorize("hasAnyAuthority('PACKING_LIST-READ')")
	public ResponseEntity<ProductionModel> getProduction(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(productionService.get(id));
	}
	
	@PostMapping("/productions/create")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-CREATE')")
	public ResponseEntity<ProductionModel>createProduction(
			@RequestBody Production production,
			HttpServletRequest request){
		production.setNo("NA");
		production.setStatus("OPEN");
		production.setCreatedBy(userService.getUserId(request));
		production.setCreatedAt(dayRepository.getCurrentBussinessDay().getId());
		production.setOpenedBy(userService.getUserId(request));
		production.setOpenedAt(dayRepository.getCurrentBussinessDay().getId());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/productions/create").toUriString());
		return ResponseEntity.created(uri).body(productionService.save(production));
	}
	
	@PostMapping("/productions/update")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-CREATE')")
	public ResponseEntity<ProductionModel>updateProduction(
			@RequestBody Production production,
			HttpServletRequest request){
		Optional<Production> prod = productionRepository.findById(production.getId());
		if(!prod.isPresent()) {
			throw new InvalidOperationException("Production could not be found in database");
		}
		if(!prod.get().getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not edit, only open production can be edited");
		}
		prod.get().setProductionName(production.getProductionName());
		prod.get().setBatchNo(production.getBatchNo());
		prod.get().setBatchSize(production.getBatchSize());
		prod.get().setUom(production.getUom());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/productions/update").toUriString());
		return ResponseEntity.created(uri).body(productionService.save(prod.get()));
	}
	
	@PostMapping("/productions/close")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-CREATE')")
	public ResponseEntity<ProductionModel>closeProduction(
			@RequestBody Production production,
			HttpServletRequest request){
		Optional<Production> prod = productionRepository.findById(production.getId());
		if(!prod.isPresent()) {
			throw new NotFoundException("Production not found in database");
		}		
		prod.get().setClosedBy(userService.getUserId(request));
		prod.get().setClosedAt(dayRepository.getCurrentBussinessDay().getId());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/productions/close").toUriString());
		return ResponseEntity.created(uri).body(productionService.close(prod.get()));
	}
	
	@PostMapping("/productions/register_material")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-CREATE')")
	public ResponseEntity<Material> registerMaterial(
			@RequestBody MaterialProduction materialProduction){
		Optional<Material> mat = materialRepository.findById(materialProduction.getMaterial().getId());
		if(!mat.isPresent()) {
			throw new NotFoundException("Material not found");
		}
		Optional<Production> prod = productionRepository.findById(materialProduction.getProduction().getId());
		if(!prod.isPresent()) {
			throw new NotFoundException("Production does not exist");
		}
		if(!prod.get().getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not process, only open production can be edited");
		}
		/**
		 * Now add material to unverified list, if it does not exist
		 */
		Optional<ProductionUnverifiedMaterial> pum = productionUnverifiedMaterialRepository.findByMaterialAndProduction(mat.get(), prod.get());
		if(!pum.isPresent()) {
			ProductionUnverifiedMaterial pm = new ProductionUnverifiedMaterial();
			pm.setMaterial(mat.get());
			pm.setProduction(prod.get());
			pm.setQty(0);
			productionUnverifiedMaterialRepository.saveAndFlush(pm);
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/productions/register_material").toUriString());
		return ResponseEntity.created(uri).body(mat.get());
	}
	
	@PostMapping("/productions/add_material")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-CREATE')")
	public ResponseEntity<Material> addMaterial(
			@RequestBody MaterialProduction materialProduction){
		Optional<Material> mat = materialRepository.findById(materialProduction.getMaterial().getId());
		if(!mat.isPresent()) {
			throw new NotFoundException("Material not found");
		}
		Optional<Production> prod = productionRepository.findById(materialProduction.getProduction().getId());
		if(!prod.isPresent()) {
			throw new NotFoundException("Production does not exist");
		}
		if(!prod.get().getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not process, only open production can be edited");
		}
		if(materialProduction.getQty() <= 0) {
			throw new InvalidEntryException("Could not add material, invalid quantity, quantity must be positive");
		}
		/**
		 * Now add material to unverified list, if it does not exist
		 */
		Optional<ProductionUnverifiedMaterial> pum = productionUnverifiedMaterialRepository.findByMaterialAndProduction(mat.get(), prod.get());
		if(pum.isPresent()) {
			pum.get().setQty(materialProduction.getQty());
			productionUnverifiedMaterialRepository.saveAndFlush(pum.get());
		}else {
			throw new InvalidOperationException("Material not registered in production");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/productions/add_material").toUriString());
		return ResponseEntity.created(uri).body(mat.get());
	}
	
	@PostMapping("/productions/add_product")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-CREATE')")
	public ResponseEntity<Product> addProduct(
			@RequestBody ProductProduction productProduction){
		Optional<Product> p = productRepository.findById(productProduction.getProduct().getId());
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		Optional<Production> prod = productionRepository.findById(productProduction.getProduction().getId());
		if(!prod.isPresent()) {
			throw new NotFoundException("Production does not exist");
		}
		if(!prod.get().getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not process, only open production can be edited");
		}
		if(productProduction.getQty() <= 0) {
			throw new InvalidEntryException("Could not add product, invalid quantity, quantity must be positive");
		}
		/**
		 * Now add material to unverified list, if it does not exist
		 */
		Optional<ProductionUnverifiedProduct> pup = productionUnverifiedProductRepository.findByProductionAndProduct(prod.get(), p.get());
		if(pup.isPresent()) {
			pup.get().setQty(productProduction.getQty());
			productionUnverifiedProductRepository.saveAndFlush(pup.get());
		}else {
			ProductionUnverifiedProduct pu = new ProductionUnverifiedProduct();
			pu.setProduct(p.get());
			pu.setProduction(prod.get());
			pu.setQty(productProduction.getQty());
			productionUnverifiedProductRepository.saveAndFlush(pu);
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/productions/add_product").toUriString());
		return ResponseEntity.created(uri).body(p.get());
	}
	
	@PostMapping("/productions/remove_material")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-CREATE')")
	public ResponseEntity<Material> removeMaterial(
			@RequestBody MaterialProduction materialProduction){
		Optional<Material> mat = materialRepository.findById(materialProduction.getMaterial().getId());
		if(!mat.isPresent()) {
			throw new NotFoundException("Material not found");
		}
		Optional<Production> prod = productionRepository.findById(materialProduction.getProduction().getId());
		if(!prod.isPresent()) {
			throw new NotFoundException("Production does not exist");
		}
		if(!prod.get().getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not process, only open production can be edited");
		}
		/**
		 * Now add material to unverified list, if it does not exist
		 */
		Optional<ProductionUnverifiedMaterial> pum = productionUnverifiedMaterialRepository.findByMaterialAndProduction(mat.get(), prod.get());
		if(pum.isPresent()) {
			productionUnverifiedMaterialRepository.delete(pum.get());
		}else {
			throw new InvalidOperationException("Material not registered in production");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/productions/remove_material").toUriString());
		return ResponseEntity.created(uri).body(mat.get());
	}	
	
	@PostMapping("/productions/verify_material")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-CREATE')")
	public ResponseEntity<Material> verifyMaterial(
			@RequestBody MaterialProduction materialProduction){
		Optional<Material> mat = materialRepository.findById(materialProduction.getMaterial().getId());
		if(!mat.isPresent()) {
			throw new NotFoundException("Material not found");
		}
		Optional<Production> prod = productionRepository.findById(materialProduction.getProduction().getId());
		if(!prod.isPresent()) {
			throw new NotFoundException("Production does not exist");
		}
		if(!prod.get().getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not process, only open production can be edited");
		}
		/**
		 * Check whether material exist in the unverified list
		 */
		Optional<ProductionUnverifiedMaterial> pum = productionUnverifiedMaterialRepository.findByMaterialAndProduction(mat.get(), prod.get());
		if(pum.isPresent()) {
			/**
			 * Check if qty matches
			 */
			if(pum.get().getQty() != materialProduction.getQty()) {
				throw new InvalidEntryException("Could not verify, quantity do not match");
			}
			
			
			Optional<ProductionMaterial> pm = productionMaterialRepository.findByMaterialAndProduction(mat.get(), prod.get());
			if(pm.isPresent()) {
				/**
				 * Add qty
				 */
				pm.get().setQty(pm.get().getQty() + materialProduction.getQty());
				productionMaterialRepository.saveAndFlush(pm.get());
			}else {
				/**
				 * Add new material
				 */
				ProductionMaterial productionMaterial = new ProductionMaterial();
				productionMaterial.setProduction(prod.get());
				productionMaterial.setMaterial(mat.get());
				productionMaterial.setQty(materialProduction.getQty());
				productionMaterialRepository.saveAndFlush(productionMaterial);
			}
			/**
			 * Deduct material from stock
			 */
			double materialStock = mat.get().getStock();
			double newMaterialStock = materialStock - materialProduction.getQty();
			mat.get().setStock(newMaterialStock);
			materialRepository.saveAndFlush(mat.get());
			/**
			 * Now, update material stock cards
			 */
			MaterialStockCard msc = new MaterialStockCard();
			msc.setQtyOut(materialProduction.getQty());		
			msc.setBalance(newMaterialStock);
			msc.setMaterial(mat.get());
			msc.setDay(dayRepository.getCurrentBussinessDay());
			msc.setReference("Used in production Ref# "+prod.get().getNo());
			materialStockCardRepository.saveAndFlush(msc);
			/**
			 * Remove from production unverified materials
			 */
			productionUnverifiedMaterialRepository.delete(pum.get());
		}else {
			throw new InvalidOperationException("Material not registered in production");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/productions/remove_material").toUriString());
		return ResponseEntity.created(uri).body(mat.get());
	}
	
	@PostMapping("/productions/remove_product")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-CREATE')")
	public ResponseEntity<Product> removeProduct(
			@RequestBody ProductProduction productProduction){
		Optional<Product> p = productRepository.findById(productProduction.getProduct().getId());
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		Optional<Production> prod = productionRepository.findById(productProduction.getProduction().getId());
		if(!prod.isPresent()) {
			throw new NotFoundException("Production does not exist");
		}
		if(!prod.get().getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not process, only open production can be edited");
		}
		/**
		 * Now add product to unverified list, if it does not exist
		 */
		Optional<ProductionUnverifiedProduct> pup = productionUnverifiedProductRepository.findByProductAndProduction(p.get(), prod.get());
		if(pup.isPresent()) {
			productionUnverifiedProductRepository.delete(pup.get());
		}else {
			throw new InvalidOperationException("Product not registered in production");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/productions/remove_product").toUriString());
		return ResponseEntity.created(uri).body(p.get());
	}	
	
	@PostMapping("/productions/verify_product")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-CREATE')")
	public ResponseEntity<Product> verifyProduct(
			@RequestBody ProductProduction productProduction){
		Optional<Product> p = productRepository.findById(productProduction.getProduct().getId());
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		Optional<Production> prod = productionRepository.findById(productProduction.getProduction().getId());
		if(!prod.isPresent()) {
			throw new NotFoundException("Production does not exist");
		}
		if(!prod.get().getStatus().equals("OPEN")) {
			throw new InvalidOperationException("Could not process, only open production can be edited");
		}
		/**
		 * Check whether material exist in the unverified list
		 */
		Optional<ProductionUnverifiedProduct> pup = productionUnverifiedProductRepository.findByProductionAndProduct(prod.get(), p.get());
		if(pup.isPresent()) {
			/**
			 * Check if qty matches
			 */
			if(pup.get().getQty() != productProduction.getQty()) {
				throw new InvalidEntryException("Could not verify, quantity do not match");
			}
			
			Optional<ProductionProduct> pp = productionProductRepository.findByProductAndProduction(p.get(), prod.get());
			if(pp.isPresent()) {
				/**
				 * Add qty
				 */
				pp.get().setQty(pp.get().getQty() + productProduction.getQty());
				productionProductRepository.saveAndFlush(pp.get());
			}else {
				/**
				 * Add new material
				 */
				ProductionProduct productionProduct = new ProductionProduct();
				productionProduct.setProduction(prod.get());
				productionProduct.setProduct(p.get());
				productionProduct.setQty(productProduction.getQty());
				productionProductRepository.saveAndFlush(productionProduct);
			}
			/**
			 * Deduct material from stock
			 */
			double productStock = p.get().getStock();
			double newProductStock = productStock - productProduction.getQty();
			p.get().setStock(newProductStock);
			productRepository.saveAndFlush(p.get());
			/**
			 * Now, update material stock cards
			 */
			ProductStockCard psc = new ProductStockCard();
			psc.setQtyOut(productProduction.getQty());		
			psc.setBalance(newProductStock);
			psc.setProduct(p.get());
			psc.setDay(dayRepository.getCurrentBussinessDay());
			psc.setReference("Produced in production Ref# "+prod.get().getNo());
			productStockCardRepository.saveAndFlush(psc);
			/**
			 * Remove from production unverified products
			 */
			productionUnverifiedProductRepository.delete(pup.get());
		}else {
			throw new InvalidOperationException("Product not registered in production");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/productions/verify_product").toUriString());
		return ResponseEntity.created(uri).body(p.get());
	}	
}

@Data
class ProductProduction {
	Product product;
	Production production;
	double qty;
}

@Data
class MaterialProduction {
	Material material;
	Production production;
	double qty;
}

