/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
import com.orbix.api.service.ProductMaterialRatioService;
import com.orbix.api.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductMaterialRatioResource {
	
	private final ProductMaterialRatioRepository productMaterialRatioRepository;
	private final DayRepository dayRepository;
	private final ProductMaterialRatioService productMaterialRatioService;
	private final UserService userService;
	private final ProductRepository productRepository;
	private final MaterialRepository materialRepository;
	
	@GetMapping("/product_material_ratios")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-READ')")
	public ResponseEntity<List<ProductMaterialRatio>>getProductMaterialRatio(){
		return ResponseEntity.ok().body(productMaterialRatioRepository.findAll());
	}
	
	@GetMapping("/product_material_ratios/get")
	//@PreAuthorize("hasAnyAuthority('PACKING_LIST-READ')")
	public ResponseEntity<ProductMaterialRatioModel> getProductMaterialRatio(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(productMaterialRatioService.get(id));
	}
	
	@GetMapping("/product_material_ratios/get_by_product")
	//@PreAuthorize("hasAnyAuthority('PACKING_LIST-READ')")
	public ResponseEntity<ProductMaterialRatioModel> getProductMaterialRatioByProduct(
			@RequestParam(name = "id") Long id){
		Optional<Product> prod = productRepository.findById(id);
		if(!prod.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		return ResponseEntity.ok().body(productMaterialRatioService.getByProduct(prod.get()));
	}
	
	@PostMapping("/product_material_ratios/create")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-CREATE')")
	public ResponseEntity<ProductMaterialRatioModel>createProductMaterialRatio(
			@RequestBody ProductMaterialRatio productMaterialRatio,
			HttpServletRequest request){
		Optional<Product> prod = productRepository.findById(productMaterialRatio.getProduct().getId());
		if(!prod.isPresent()) {
			throw new InvalidOperationException("Product not found");
		}
		Optional<Material> mat = materialRepository.findById(productMaterialRatio.getProduct().getId());
		if(!mat.isPresent()) {
			throw new InvalidOperationException("Material not found");
		}		
		Optional<ProductMaterialRatio> pmr = productMaterialRatioRepository.findByProductAndMaterial(prod.get(), mat.get());
		if(pmr.isPresent()) {
			throw new NotFoundException("Ratio already exists, consider editing the respective ratio.");
		}
		Optional<ProductMaterialRatio> product = productMaterialRatioRepository.findByProduct(prod.get());
		if(!product.isPresent()) {
			throw new InvalidOperationException("The product already exist in the list. Please consider editing the ratio");
		}
		List<Material> material = productMaterialRatioRepository.findByMaterial(mat.get());
		if(!material.isEmpty()) {
			throw new InvalidOperationException("The material already exist in the list. Please consider editing the ratio");
		}
		productMaterialRatio.setCreatedBy(userService.getUserId(request));
		productMaterialRatio.setCreatedAt(dayRepository.getCurrentBussinessDay().getId());		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/product_material_ratios/create").toUriString());
		return ResponseEntity.created(uri).body(productMaterialRatioService.save(productMaterialRatio));
	}
	
	@PutMapping("/product_material_ratios/update")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-CREATE')")
	public ResponseEntity<ProductMaterialRatioModel>updateProductMaterialRatio(
			@RequestBody ProductMaterialRatio productMaterialRatio,
			HttpServletRequest request){
		Optional<Product> prod = productRepository.findById(productMaterialRatio.getProduct().getId());
		if(!prod.isPresent()) {
			throw new InvalidOperationException("Product not found");
		}
		Optional<Material> mat = materialRepository.findById(productMaterialRatio.getProduct().getId());
		if(!mat.isPresent()) {
			throw new InvalidOperationException("Material not found");
		}		
		Optional<ProductMaterialRatio> pmr = productMaterialRatioRepository.findByProductAndMaterial(prod.get(), mat.get());
		if(!pmr.isPresent()) {
			throw new NotFoundException("Ratio not found in database");
		}
		productMaterialRatio.setCreatedBy(userService.getUserId(request));
		productMaterialRatio.setCreatedAt(dayRepository.getCurrentBussinessDay().getId());		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product_material_ratios/update").toUriString());
		return ResponseEntity.created(uri).body(productMaterialRatioService.save(productMaterialRatio));
	}
	
	@DeleteMapping("/product_material_ratios/delete")
	//@PreAuthorize("hasAnyAuthority('PRODUCTION-CREATE')")
	public ResponseEntity<Boolean>deleteProductMaterialRatio(
			@RequestParam(name = "id") Long id){				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product_material_ratios/delete").toUriString());
		return ResponseEntity.created(uri).body(productMaterialRatioService.delete(id));
	}
	
}
