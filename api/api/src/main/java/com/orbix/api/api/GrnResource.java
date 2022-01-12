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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.domain.Grn;
import com.orbix.api.domain.GrnDetail;
import com.orbix.api.domain.Lpo;
import com.orbix.api.domain.LpoDetail;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.Supplier;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.GrnDetailModel;
import com.orbix.api.models.GrnModel;
import com.orbix.api.models.LpoDetailModel;
import com.orbix.api.models.LpoModel;
import com.orbix.api.repositories.GrnDetailRepository;
import com.orbix.api.repositories.GrnRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.SupplierRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.GrnService;
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
public class GrnResource {
	
	private final 	UserService userService;
	private final 	DayService dayService;
	private final 	GrnService grnService;
	private final 	GrnRepository grnRepository;
	private final 	GrnDetailRepository grnDetailRepository;
	private final 	SupplierRepository supplierRepository;
	private final 	ProductRepository productRepository;
	
	@GetMapping("/grns")
	@PreAuthorize("hasAnyAuthority('GRN-READ')")
	public ResponseEntity<List<GrnModel>>getGrns(){
		return ResponseEntity.ok().body(grnService.getAllVisible());
	}
	
	@GetMapping("/grns/get")
	@PreAuthorize("hasAnyAuthority('GRN-READ')")
	public ResponseEntity<GrnModel> getGrn(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(grnService.get(id));
	}
	
	@GetMapping("/grns/get_by_no")
	@PreAuthorize("hasAnyAuthority('GRN-READ')")
	public ResponseEntity<GrnModel> getGrnByNo(
			@RequestParam(name = "no") String no){
		return ResponseEntity.ok().body(grnService.getByNo(no));
	}
	
	@GetMapping("/grn_details/get_by_grn")
	@PreAuthorize("hasAnyAuthority('GRN-READ')")
	public ResponseEntity<List<GrnDetailModel>>getGrnDetails(
			@RequestParam(name = "id") Long id){		
		return ResponseEntity.ok().body(grnService.getAllDetails(grnRepository.findById(id).get()));
	}
	
	@PostMapping("/grns/create")
	@PreAuthorize("hasAnyAuthority('GRN-CREATE')")
	public ResponseEntity<GrnModel>createGrn(
			@RequestBody Grn grn,
			HttpServletRequest request){
		grn.setNo("NA");
		grn.setStatus("PENDING");
		grn.setCreatedBy(userService.getUserId(request));
		grn.setCreatedAt(dayService.getDayId());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/grns/create").toUriString());
		return ResponseEntity.created(uri).body(grnService.save(grn));
	}
	
	@PutMapping("/grns/update")
	@PreAuthorize("hasAnyAuthority('GRN-UPDATE')")
	public ResponseEntity<GrnModel>updateGrn(
			@RequestBody Grn grn,
			HttpServletRequest request){
		Optional<Grn> g = grnRepository.findById(grn.getId());
		if(!g.isPresent()) {
			throw new NotFoundException("GRN not found");
		}
		if(!g.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only Pending GRNs can be edited");
		}
		if(!grn.getLpo().getNo().equals(g.get().getLpo().getNo())) {
			throw new InvalidOperationException("Could not process, changing order no is not allowed");
		}
		
		g.get().setGrnDate(grn.getGrnDate());
		g.get().setInvoiceNo(grn.getInvoiceNo());
		g.get().setInvoiceAmount(grn.getInvoiceAmount());
		g.get().setComments(grn.getComments());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/grns/update").toUriString());
		return ResponseEntity.created(uri).body(grnService.save(g.get()));
	}
	
	@PutMapping("/grns/approve")
	@PreAuthorize("hasAnyAuthority('GRN-APPROVE')")
	public ResponseEntity<GrnModel>approveGrn(
			@RequestBody Grn grn,
			HttpServletRequest request){		
		Optional<Grn> g = grnRepository.findById(grn.getId());
		if(!g.isPresent()) {
			throw new NotFoundException("GRN not found");
		}
		/**
		 * Validation Approval is done in the GrnService
		 * This has been done to reduce control logic in this controller and to increase moludarity
		 */
		g.get().setApprovedBy(userService.getUserId(request));
		g.get().setApprovedAt(dayService.getDayId());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/grn/approve").toUriString());
		return ResponseEntity.created(uri).body(grnService.receive(g.get()));
	}
	
	@PutMapping("/grns/archive")
	@PreAuthorize("hasAnyAuthority('GRN-CREATE','GRN-UPDATE','GRN-ARCHIVE')")
	public ResponseEntity<Boolean>archiveGrn(
			@RequestBody Grn grn,
			HttpServletRequest request){		
		Optional<Grn> g = grnRepository.findById(grn.getId());
		if(!g.isPresent()) {
			throw new NotFoundException("GRN not found");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/grns/archive").toUriString());
		return ResponseEntity.created(uri).body(grnService.archive(g.get()));
	}
	
	@PutMapping("/grns/archive_all")
	@PreAuthorize("hasAnyAuthority('GRN-CREATE','GRN-UPDATE','GRN-ARCHIVE')")
	public ResponseEntity<Boolean>archiveGrns(){			
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/grns/archive_all").toUriString());
		return ResponseEntity.created(uri).body(grnService.archiveAll());
	}
	
	@GetMapping("/grn_details/get")
	@PreAuthorize("hasAnyAuthority('GRN-CREATE','GRN-UPDATE')")
	public ResponseEntity<GrnDetailModel>getDetail(
			@RequestParam(name = "id") Long id){		
		Optional<GrnDetail> d = grnDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}		
		GrnDetailModel detail = new GrnDetailModel();
		detail.setId(d.get().getId());
		detail.setQtyOrdered(d.get().getQtyOrdered());
		detail.setQtyReceived(d.get().getQtyReceived());
		detail.setClientPriceVatIncl(d.get().getClientPriceVatIncl());
		detail.setClientPriceVatExcl(d.get().getClientPriceVatExcl());
		detail.setSupplierPriceVatIncl(d.get().getSupplierPriceVatIncl());
		detail.setSupplierPriceVatExcl(d.get().getSupplierPriceVatExcl());
		detail.setProduct(d.get().getProduct());		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/grn_details/get").toUriString());
		return ResponseEntity.created(uri).body(detail);
	}
	
	@PostMapping("/grn_details/save")
	@PreAuthorize("hasAnyAuthority('GRN-CREATE','GRN-UPDATE')")
	public ResponseEntity<GrnDetailModel>createGrnDetail(
			@RequestBody GrnDetail grnDetail){
		if(grnDetail.getQtyReceived() < 0) {
			throw new InvalidEntryException("Quantity value should be positive");
		}
		Optional<Grn> g = grnRepository.findById(grnDetail.getGrn().getId());
		if(!g.isPresent()) {
			throw new NotFoundException("Could not process, GRN not found");
		}
		if(!g.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing is not allowed, only Pending GRNs can be edited.");
		}
		
		Optional<GrnDetail> d = grnDetailRepository.findById(grnDetail.getId());
		if(!d.isPresent()) {
			throw new NotFoundException("Could not process, detail not found");
		}		
		GrnDetail detail = new GrnDetail();
		if(d.isPresent()) {
			/**
			 * Update existing detail
			 * First validate inputs, if valid, update, else, reject
			 */
			detail = d.get();
			if(grnDetail.getQtyReceived() > detail.getQtyOrdered()) {
				throw new InvalidOperationException("Could not process, Quantity received exceeds quantity ordered");
			}
			detail.setQtyReceived(grnDetail.getQtyReceived());
			detail.setSupplierPriceVatIncl(grnDetail.getSupplierPriceVatIncl());				
		}	
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/grn_details/save").toUriString());
		return ResponseEntity.created(uri).body(grnService.saveDetail(detail));
	}

}
