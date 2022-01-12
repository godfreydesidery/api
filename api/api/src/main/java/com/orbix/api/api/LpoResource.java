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

import com.orbix.api.domain.Grn;
import com.orbix.api.domain.Lpo;
import com.orbix.api.domain.LpoDetail;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.Supplier;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.LpoDetailModel;
import com.orbix.api.models.LpoModel;
import com.orbix.api.repositories.LpoDetailRepository;
import com.orbix.api.repositories.LpoRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.SupplierRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.LpoService;
import com.orbix.api.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LpoResource {
	private final 	UserService userService;
	private final 	DayService dayService;
	private final 	LpoService lpoService;
	private final 	LpoRepository lpoRepository;
	private final 	LpoDetailRepository lpoDetailRepository;
	private final 	SupplierRepository supplierRepository;
	private final 	ProductRepository productRepository;
	
	@GetMapping("/lpos")
	@PreAuthorize("hasAnyAuthority('LPO-READ')")
	public ResponseEntity<List<LpoModel>>getLpos(){
		return ResponseEntity.ok().body(lpoService.getAllVisible());
	}
	
	@GetMapping("/lpos/get")
	@PreAuthorize("hasAnyAuthority('LPO-READ')")
	public ResponseEntity<LpoModel> getLpo(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(lpoService.get(id));
	}
	
	@GetMapping("/lpos/get_by_no")
	@PreAuthorize("hasAnyAuthority('LPO-READ')")
	public ResponseEntity<LpoModel> getLpoByNo(
			@RequestParam(name = "no") String no){
		return ResponseEntity.ok().body(lpoService.getByNo(no));
	}
	
	@GetMapping("/lpo_details/get_by_lpo")
	@PreAuthorize("hasAnyAuthority('LPO-READ')")
	public ResponseEntity<List<LpoDetailModel>>getLpoDetails(
			@RequestParam(name = "id") Long id){		
		return ResponseEntity.ok().body(lpoService.getAllDetails(lpoRepository.findById(id).get()));
	}
	
	@PostMapping("/lpos/create")
	@PreAuthorize("hasAnyAuthority('LPO-CREATE')")
	public ResponseEntity<LpoModel>createLpo(
			@RequestBody Lpo lpo,
			HttpServletRequest request){
		Optional<Supplier> s = supplierRepository.findByCode(lpo.getSupplier().getCode());
		if(!s.isPresent()) {
			throw new NotFoundException("Supplier not found");
		}
		Lpo l = new Lpo();
		l.setNo("NA");
		l.setSupplier(s.get());
		l.setStatus("BLANK");
		l.setOrderDate(lpo.getOrderDate());
		l.setValidityDays(lpo.getValidityDays());
		l.setValidUntil(lpo.getValidUntil());
		l.setComments(lpo.getComments());
		l.setCreatedBy(userService.getUserId(request));
		l.setCreatedAt(dayService.getDayId());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/lpos/create").toUriString());
		return ResponseEntity.created(uri).body(lpoService.save(l));
	}
	
	@PutMapping("/lpos/update")
	@PreAuthorize("hasAnyAuthority('LPO-UPDATE')")
	public ResponseEntity<LpoModel>updateLpo(
			@RequestBody Lpo lpo,
			HttpServletRequest request){
		Optional<Supplier> s = supplierRepository.findByCode(lpo.getSupplier().getCode());
		if(!s.isPresent()) {
			throw new NotFoundException("Supplier not found");
		}
		Optional<Lpo> l = lpoRepository.findById(lpo.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("LPO not found");
		}
		if(!l.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only Pending LPOs can be edited");
		}
		List<LpoDetail> d = lpoDetailRepository.findByLpo(l.get());
		int i = 0;
		for(LpoDetail dt : d) {
			i = 1;
			break;
		}
		if(i > 0 && !l.get().getSupplier().equals(s.get())) {
			throw new InvalidOperationException("Changing Supplier is not allowed for non blank LPO");
		}
		
		l.get().setSupplier(s.get());
		l.get().setOrderDate(lpo.getOrderDate());
		l.get().setValidityDays(lpo.getValidityDays());
		l.get().setValidUntil(lpo.getValidUntil());
		l.get().setComments(lpo.getComments());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/lpos/update").toUriString());
		return ResponseEntity.created(uri).body(lpoService.save(l.get()));
	}
	
	@PutMapping("/lpos/approve")
	@PreAuthorize("hasAnyAuthority('LPO-APPROVE')")
	public ResponseEntity<LpoModel>approveLpo(
			@RequestBody Lpo lpo,
			HttpServletRequest request){		
		Optional<Lpo> l = lpoRepository.findById(lpo.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("LPO not found");
		}
		if(l.get().getStatus().equals("PENDING")) {
			l.get().setApprovedBy(userService.getUserId(request));
			l.get().setApprovedAt(dayService.getDayId());
			l.get().setStatus("APPROVED");
		}else {
			throw new InvalidOperationException("Could not approve, not a PENDING LPO");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/lpos/approve").toUriString());
		return ResponseEntity.created(uri).body(lpoService.save(l.get()));
	}
	
	@PutMapping("/lpos/print")
	@PreAuthorize("hasAnyAuthority('LPO-PRINT')")
	public ResponseEntity<LpoModel>printLpo(
			@RequestBody Lpo lpo,
			HttpServletRequest request){		
		Optional<Lpo> l = lpoRepository.findById(lpo.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("LPO not found");
		}
		if(l.get().getStatus().equals("APPROVED")) {
			l.get().setPrintedBy(userService.getUserId(request));
			l.get().setPrintedAt(dayService.getDayId());
			l.get().setStatus("PRINTED");
		}else {
			throw new InvalidOperationException("Could not print, LPO not approved");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/lpos/print").toUriString());
		return ResponseEntity.created(uri).body(lpoService.save(l.get()));
	}
	
	@PutMapping("/lpos/cancel")
	@PreAuthorize("hasAnyAuthority('LPO-CANCEL')")
	public ResponseEntity<LpoModel>cancelLpo(
			@RequestBody Lpo lpo,
			HttpServletRequest request){		
		Optional<Lpo> l = lpoRepository.findById(lpo.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("LPO not found");
		}
		if(l.get().getStatus().equals("PENDING") || l.get().getStatus().equals("APPROVED")) {
			l.get().setPrintedBy(userService.getUserId(request));
			l.get().setPrintedAt(dayService.getDayId());
			l.get().setStatus("CANCELED");
		}else {
			throw new InvalidOperationException("Could not cancel, only Pending or Approved LPOs can be canceled");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/lpos/cancel").toUriString());
		return ResponseEntity.created(uri).body(lpoService.save(l.get()));
	}
	
	@PutMapping("/lpos/archive")
	@PreAuthorize("hasAnyAuthority('LPO-CREATE','LPO-UPDATE','LPO-ARCHIVE')")
	public ResponseEntity<Boolean>archiveLpo(
			@RequestBody Lpo lpo,
			HttpServletRequest request){		
		Optional<Lpo> l = lpoRepository.findById(lpo.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("LPO not found");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/lpos/archive").toUriString());
		return ResponseEntity.created(uri).body(lpoService.archive(l.get()));
	}
	
	@PutMapping("/lpos/archive_all")
	@PreAuthorize("hasAnyAuthority('LPO-CREATE','LPO-UPDATE','LPO-ARCHIVE')")
	public ResponseEntity<Boolean>archiveLpos(){			
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/lpos/archive_all").toUriString());
		return ResponseEntity.created(uri).body(lpoService.archiveAll());
	}
	
	@PostMapping("/lpo_details/save")
	@PreAuthorize("hasAnyAuthority('LPO-CREATE','LPO-UPDATE')")
	public ResponseEntity<LpoDetailModel>createLpoDetail(
			@RequestBody LpoDetail lpoDetail){
		if(lpoDetail.getQty() <= 0) {
			throw new InvalidEntryException("Quantity value should be more than 0");
		}
		Optional<Lpo> l = lpoRepository.findById(lpoDetail.getLpo().getId());
		if(!l.isPresent()) {
			throw new NotFoundException("LPO not found");
		}
		if(l.get().getStatus().equals("BLANK")) {
			l.get().setStatus("PENDING");
			lpoRepository.saveAndFlush(l.get());
		}
		if(!(l.get().getStatus().equals("PENDING") || l.get().getStatus().equals("BLANK"))) {
			throw new InvalidOperationException("Editing is not allowed, only PENDING or BLANK LPOs can be edited.");
		}
		Optional<Product> p = productRepository.findById(lpoDetail.getProduct().getId());
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		Optional<LpoDetail> d = lpoDetailRepository.findByLpoAndProduct(l.get(), p.get());
		LpoDetail detail = new LpoDetail();
		if(d.isPresent()) {
			/**
			 * Update existing detail
			 */
			detail = d.get();
			detail.setQty(lpoDetail.getQty());
			detail.setCostPriceVatIncl(lpoDetail.getCostPriceVatIncl());
			detail.setCostPriceVatExcl(lpoDetail.getCostPriceVatExcl());			
		}else {
			/**
			 * Create new detail
			 */
			detail.setLpo(l.get());
			detail.setProduct(lpoDetail.getProduct());
			detail.setQty(lpoDetail.getQty());
			detail.setCostPriceVatIncl(lpoDetail.getCostPriceVatIncl());
			detail.setCostPriceVatExcl(lpoDetail.getCostPriceVatExcl());
		}		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/lpo_details/save").toUriString());
		return ResponseEntity.created(uri).body(lpoService.saveDetail(detail));
	}
	
	@GetMapping("/lpo_details/get_by_product_id_and_lpo_id")
	@PreAuthorize("hasAnyAuthority('LPO-CREATE','LPO-UPDATE')")
	public ResponseEntity<LpoDetailModel>getLpoDetailByProductAndLpo(
			@RequestParam(name = "product_id") Long productId,
			@RequestParam(name = "lpo_id") Long lpoId){
		Optional<Lpo> l = lpoRepository.findById(lpoId);
		if(!l.isPresent()) {
			throw new NotFoundException("LPO not found");
		}
		Optional<Product> p = productRepository.findById(lpoId);
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		Optional<LpoDetail> d = lpoDetailRepository.findByLpoAndProduct(l.get(), p.get());
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}		
		LpoDetailModel detail = new LpoDetailModel();
		detail.setCostPriceVatIncl(d.get().getCostPriceVatIncl());
		detail.setCostPriceVatExcl(d.get().getCostPriceVatExcl());
		detail.setQty(d.get().getQty());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/lpo_details/get_by_product_id_and_lpo_id").toUriString());
		return ResponseEntity.created(uri).body(detail);
	}
	
	@GetMapping("/lpo_details/get")
	@PreAuthorize("hasAnyAuthority('LPO-CREATE','LPO-UPDATE')")
	public ResponseEntity<LpoDetailModel>getDetail(
			@RequestParam(name = "id") Long id){		
		Optional<LpoDetail> d = lpoDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}		
		LpoDetailModel detail = new LpoDetailModel();
		detail.setCostPriceVatIncl(d.get().getCostPriceVatIncl());
		detail.setCostPriceVatExcl(d.get().getCostPriceVatExcl());
		detail.setQty(d.get().getQty());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/lpo_details/get").toUriString());
		return ResponseEntity.created(uri).body(detail);
	}
	
	@DeleteMapping("/lpo_details/delete")
	@PreAuthorize("hasAnyAuthority('LPO-CREATE','LPO-UPDATE')")
	public ResponseEntity<Boolean> deleteDetail(
			@RequestParam(name = "id") Long id){		
		Optional<LpoDetail> d = lpoDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}
		Lpo lpo = d.get().getLpo();
		if(!lpo.getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only pending LPO can be edited");
		}		
		lpoDetailRepository.delete(d.get());		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/lpo_details/delete").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
}
