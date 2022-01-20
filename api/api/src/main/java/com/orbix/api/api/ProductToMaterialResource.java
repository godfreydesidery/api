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
import com.orbix.api.domain.ProductToMaterial;
import com.orbix.api.domain.ProductToMaterialDetail;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.ProductToMaterialDetailModel;
import com.orbix.api.models.ProductToMaterialModel;
import com.orbix.api.repositories.MaterialRepository;
import com.orbix.api.repositories.ProductMaterialRatioRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.ProductToMaterialDetailRepository;
import com.orbix.api.repositories.ProductToMaterialRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.ProductToMaterialService;
import com.orbix.api.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductToMaterialResource {
	private final UserService userService;
	private final DayService dayService;	
	private final ProductToMaterialRepository productToMaterialRepository;
	private final ProductToMaterialDetailRepository productToMaterialDetailRepository;
	private final ProductRepository productRepository;
	private final MaterialRepository materialRepository;
	private final ProductToMaterialService productToMaterialService;
	private final ProductMaterialRatioRepository productMaterialRatioRepository;
	
	@GetMapping("/product_to_materials")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-READ')")
	public ResponseEntity<List<ProductToMaterialModel>>getProductToMaterials(){
		return ResponseEntity.ok().body(productToMaterialService.getAllVisible());
	}
	
	@GetMapping("/product_to_materials/customer")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-READ')")
	public ResponseEntity<List<ProductToMaterialModel>>getProductToMaterials(
			@RequestParam(name = "id") Long id){		
		return ResponseEntity.ok().body(productToMaterialService.getAllVisible());
	}
	
	@GetMapping("/product_to_materials/get")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-READ')")
	public ResponseEntity<ProductToMaterialModel> getProductToMaterial(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(productToMaterialService.get(id));
	}
	
	@GetMapping("/product_to_materials/get_by_no")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-READ')")
	public ResponseEntity<ProductToMaterialModel> getProductToMaterialByNo(
			@RequestParam(name = "no") String no){
		return ResponseEntity.ok().body(productToMaterialService.getByNo(no));
	}
	
	@GetMapping("/product_to_material_details/get_by_productToMaterial")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-READ')")
	public ResponseEntity<List<ProductToMaterialDetailModel>>getProductToMaterialDetails(
			@RequestParam(name = "id") Long id){		
		return ResponseEntity.ok().body(productToMaterialService.getAllDetails(productToMaterialRepository.findById(id).get()));
	}
	
	@PostMapping("/product_to_materials/create")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-CREATE')")
	public ResponseEntity<ProductToMaterialModel>createProductToMaterial(
			@RequestBody ProductToMaterial productToMaterial,
			HttpServletRequest request){		
		ProductToMaterial inv = new ProductToMaterial();
		inv.setNo("NA");
		inv.setStatus("BLANK");
		inv.setComments(productToMaterial.getComments());	
		inv.setCreatedBy(userService.getUserId(request));
		inv.setCreatedAt(dayService.getDayId());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product_to_materials/create").toUriString());
		return ResponseEntity.created(uri).body(productToMaterialService.save(inv));
	}
	
	@PutMapping("/product_to_materials/update")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-UPDATE')")
	public ResponseEntity<ProductToMaterialModel>updateProductToMaterial(
			@RequestBody ProductToMaterial productToMaterial,
			HttpServletRequest request){
		Optional<ProductToMaterial> l = productToMaterialRepository.findById(productToMaterial.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("CONVERSION not found");
		}
		if(!l.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only Pending Sales Invoices can be edited");
		}
		List<ProductToMaterialDetail> d = productToMaterialDetailRepository.findByProductToMaterial(l.get());			
		l.get().setComments(productToMaterial.getComments());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product_to_materials/update").toUriString());
		return ResponseEntity.created(uri).body(productToMaterialService.save(l.get()));
	}
	
	@PutMapping("/product_to_materials/approve")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-APPROVE')")
	public ResponseEntity<ProductToMaterialModel>approveProductToMaterial(
			@RequestBody ProductToMaterial productToMaterial,
			HttpServletRequest request){		
		Optional<ProductToMaterial> l = productToMaterialRepository.findById(productToMaterial.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("CONVERSION not found");
		}
		if(l.get().getStatus().equals("PENDING")) {
			l.get().setApprovedBy(userService.getUserId(request));
			l.get().setApprovedAt(dayService.getDayId());
			l.get().setStatus("APPROVED");
		}else {
			throw new InvalidOperationException("Could not approve, not a PENDING CONVERSION");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product_to_materials/approve").toUriString());
		return ResponseEntity.created(uri).body(productToMaterialService.post(l.get()));
	}
	
	@PutMapping("/product_to_materials/cancel")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-CANCEL')")
	public ResponseEntity<ProductToMaterialModel>cancelProductToMaterial(
			@RequestBody ProductToMaterial productToMaterial,
			HttpServletRequest request){		
		Optional<ProductToMaterial> l = productToMaterialRepository.findById(productToMaterial.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("CONVERSION not found");
		}
		if(l.get().getStatus().equals("PENDING") || l.get().getStatus().equals("BLANK")) {
			l.get().setStatus("CANCELED");
		}else {
			throw new InvalidOperationException("Could not cancel, only Pending or Approved CONVERSIONs can be canceled");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product_to_materials/cancel").toUriString());
		return ResponseEntity.created(uri).body(productToMaterialService.save(l.get()));
	}
	
	@PutMapping("/product_to_materials/archive")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-CREATE','CONVERSION-UPDATE','CONVERSION-ARCHIVE')")
	public ResponseEntity<Boolean>archiveProductToMaterial(
			@RequestBody ProductToMaterial productToMaterial,
			HttpServletRequest request){		
		Optional<ProductToMaterial> l = productToMaterialRepository.findById(productToMaterial.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("CONVERSION not found");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product_to_materials/archive").toUriString());
		return ResponseEntity.created(uri).body(productToMaterialService.archive(l.get()));
	}
	
	@PutMapping("/product_to_materials/archive_all")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-CREATE','CONVERSION-UPDATE','CONVERSION-ARCHIVE')")
	public ResponseEntity<Boolean>archiveProductToMaterials(){			
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product_to_materials/archive_all").toUriString());
		return ResponseEntity.created(uri).body(productToMaterialService.archiveAll());
	}
	
	@PostMapping("/product_to_material_details/save")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-CREATE','CONVERSION-UPDATE')")
	public ResponseEntity<ProductToMaterialDetailModel>createProductToMaterialDetail(
			@RequestBody ProductToMaterialDetail productToMaterialDetail){
		if(productToMaterialDetail.getQty() <= 0) {
			throw new InvalidEntryException("Quantity value should be more than 0");
		}
		Optional<ProductToMaterial> l = productToMaterialRepository.findById(productToMaterialDetail.getProductToMaterial().getId());
		if(!l.isPresent()) {
			throw new NotFoundException("CONVERSION not found");
		}
		if(l.get().getStatus().equals("BLANK")) {
			l.get().setStatus("PENDING");
			productToMaterialRepository.saveAndFlush(l.get());
		}
		if(!(l.get().getStatus().equals("PENDING") || l.get().getStatus().equals("BLANK"))) {
			throw new InvalidOperationException("Editing is not allowed, only PENDING or BLANK CONVERSIONs can be edited.");
		}
		Optional<Product> p = productRepository.findById(productToMaterialDetail.getProduct().getId());
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		Optional<Material> m = materialRepository.findById(productToMaterialDetail.getMaterial().getId());
		if(!m.isPresent()) {
			throw new NotFoundException("Material not found");
		}
		Optional<ProductToMaterialDetail> d = productToMaterialDetailRepository.findByProductAndProductToMaterial(p.get(), l.get());
		Optional<ProductMaterialRatio> pmr = productMaterialRatioRepository.findByProductAndMaterial(p.get(), m.get());
		if(!pmr.isPresent()) {
			throw new NotFoundException("Product-Material ratio not found");
		}
		if(productToMaterialDetail.getRatio() != pmr.get().getRatio()) {
			throw new InvalidOperationException("Could not save, ratio mismatch");
		}
		ProductToMaterialDetail detail = new ProductToMaterialDetail();
		if(d.isPresent()) {
			/**
			 * Update existing detail
			 */
			detail = d.get();			
			detail.setQty(productToMaterialDetail.getQty());
			detail.setRatio(productToMaterialDetail.getRatio());
		}else {
			/**
			 * Create new detail
			 */
			detail.setProductToMaterial(l.get());
			detail.setProduct(productToMaterialDetail.getProduct());
			detail.setMaterial(productToMaterialDetail.getMaterial());
			detail.setQty(productToMaterialDetail.getQty());
			detail.setRatio(productToMaterialDetail.getRatio());
		}		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product_to_material_details/save").toUriString());
		return ResponseEntity.created(uri).body(productToMaterialService.saveDetail(detail));
	}
	
	@GetMapping("/product_to_material_details/get")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-CREATE','CONVERSION-UPDATE')")
	public ResponseEntity<ProductToMaterialDetailModel>getDetail(
			@RequestParam(name = "id") Long id){		
		Optional<ProductToMaterialDetail> d = productToMaterialDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}		
		ProductToMaterialDetailModel detail = new ProductToMaterialDetailModel();
		detail.setRatio(d.get().getRatio());
		detail.setQty(d.get().getQty());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product_to_material_details/get").toUriString());
		return ResponseEntity.created(uri).body(detail);
	}
	
	@DeleteMapping("/product_to_material_details/delete")
	//@PreAuthorize("hasAnyAuthority('CONVERSION-CREATE','CONVERSION-UPDATE')")
	public ResponseEntity<Boolean> deleteDetail(
			@RequestParam(name = "id") Long id){		
		Optional<ProductToMaterialDetail> d = productToMaterialDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}
		ProductToMaterial productToMaterial = d.get().getProductToMaterial();
		if(!productToMaterial.getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only pending CONVERSION can be edited");
		}		
		productToMaterialDetailRepository.delete(d.get());		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product_to_material_details/delete").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
}
