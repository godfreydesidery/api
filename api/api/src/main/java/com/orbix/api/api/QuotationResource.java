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

import com.orbix.api.domain.Customer;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.Quotation;
import com.orbix.api.domain.QuotationDetail;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.QuotationDetailModel;
import com.orbix.api.models.QuotationModel;
import com.orbix.api.repositories.CustomerRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.QuotationDetailRepository;
import com.orbix.api.repositories.QuotationRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.ProductService;
import com.orbix.api.service.QuotationService;
import com.orbix.api.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuotationResource {
	
	private final 	UserService userService;
	private final 	DayService dayService;
	private final 	QuotationService quotationService;
	private final 	QuotationRepository quotationRepository;
	private final 	QuotationDetailRepository quotationDetailRepository;
	private final 	CustomerRepository customerRepository;
	private final 	ProductRepository productRepository;
	
	@GetMapping("/quotations")
	@PreAuthorize("hasAnyAuthority('QUOTATION-READ')")
	public ResponseEntity<List<QuotationModel>>getQuotations(){
		return ResponseEntity.ok().body(quotationService.getAllVisible());
	}
	
	@GetMapping("/quotations/get")
	@PreAuthorize("hasAnyAuthority('QUOTATION-READ')")
	public ResponseEntity<QuotationModel> getQuotation(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(quotationService.get(id));
	}
	
	@GetMapping("/quotations/get_by_no")
	@PreAuthorize("hasAnyAuthority('QUOTATION-READ')")
	public ResponseEntity<QuotationModel> getQuotationByNo(
			@RequestParam(name = "no") String no){
		return ResponseEntity.ok().body(quotationService.getByNo(no));
	}
	
	@GetMapping("/quotation_details/get_by_quotation")
	@PreAuthorize("hasAnyAuthority('QUOTATION-READ')")
	public ResponseEntity<List<QuotationDetailModel>>getQuotationDetails(
			@RequestParam(name = "id") Long id){		
		return ResponseEntity.ok().body(quotationService.getAllDetails(quotationRepository.findById(id).get()));
	}
	
	@PostMapping("/quotations/create")
	@PreAuthorize("hasAnyAuthority('QUOTATION-CREATE')")
	public ResponseEntity<QuotationModel>createQuotation(
			@RequestBody Quotation quotation,
			HttpServletRequest request){
		Optional<Customer> c = customerRepository.findByNo(quotation.getCustomer().getNo());
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found");
		}
		Quotation quot = new Quotation();
		quot.setNo("NA");
		quot.setCustomer(c.get());
		quot.setStatus("BLANK");
		quot.setQuotationDate(quotation.getQuotationDate());
		quot.setComments(quotation.getComments());	
		quot.setCreatedBy(userService.getUserId(request));
		quot.setCreatedAt(dayService.getDayId());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/quotations/create").toUriString());
		return ResponseEntity.created(uri).body(quotationService.save(quot));
	}
	
	@PutMapping("/quotations/update")
	@PreAuthorize("hasAnyAuthority('QUOTATION-UPDATE')")
	public ResponseEntity<QuotationModel>updateQuotation(
			@RequestBody Quotation quotation,
			HttpServletRequest request){
		Optional<Customer> c = customerRepository.findByNo(quotation.getCustomer().getNo());
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found");
		}
		Optional<Quotation> l = quotationRepository.findById(quotation.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("QUOTATION not found");
		}
		if(!l.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only Pending Sales Quotations can be edited");
		}
		List<QuotationDetail> d = quotationDetailRepository.findByQuotation(l.get());
		int i = 0;
		for(@SuppressWarnings("unused") QuotationDetail dt : d) {
			i = 1;
			break;
		}
		if(i > 0 && !l.get().getCustomer().equals(c.get())) {
			throw new InvalidOperationException("Changing Customer is not allowed for non blank Sales Quotations");
		}		
		l.get().setCustomer(c.get());
		l.get().setQuotationDate(quotation.getQuotationDate());
		l.get().setComments(quotation.getComments());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/quotations/update").toUriString());
		return ResponseEntity.created(uri).body(quotationService.save(l.get()));
	}
	
	@PutMapping("/quotations/approve")
	@PreAuthorize("hasAnyAuthority('QUOTATION-APPROVE')")
	public ResponseEntity<QuotationModel>approveQuotation(
			@RequestBody Quotation quotation,
			HttpServletRequest request){		
		Optional<Quotation> l = quotationRepository.findById(quotation.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("QUOTATION not found");
		}
		if(l.get().getStatus().equals("PENDING")) {
			l.get().setApprovedBy(userService.getUserId(request));
			l.get().setApprovedAt(dayService.getDayId());
			l.get().setStatus("APPROVED");
		}else {
			throw new InvalidOperationException("Could not approve, not a PENDING QUOTATION");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/quotations/approve").toUriString());
		return ResponseEntity.created(uri).body(quotationService.save(l.get()));
	}
	
	@PutMapping("/quotations/cancel")
	@PreAuthorize("hasAnyAuthority('QUOTATION-CANCEL')")
	public ResponseEntity<QuotationModel>cancelQuotation(
			@RequestBody Quotation quotation,
			HttpServletRequest request){		
		Optional<Quotation> l = quotationRepository.findById(quotation.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("QUOTATION not found");
		}
		if(l.get().getStatus().equals("PENDING") || l.get().getStatus().equals("BLANK")) {
			l.get().setStatus("CANCELED");
		}else {
			throw new InvalidOperationException("Could not cancel, only Pending or Approved QUOTATIONs can be canceled");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/quotations/cancel").toUriString());
		return ResponseEntity.created(uri).body(quotationService.save(l.get()));
	}
	
	@PutMapping("/quotations/archive")
	@PreAuthorize("hasAnyAuthority('QUOTATION-CREATE','QUOTATION-UPDATE','QUOTATION-ARCHIVE')")
	public ResponseEntity<Boolean>archiveQuotation(
			@RequestBody Quotation quotation,
			HttpServletRequest request){		
		Optional<Quotation> l = quotationRepository.findById(quotation.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("QUOTATION not found");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/quotations/archive").toUriString());
		return ResponseEntity.created(uri).body(quotationService.archive(l.get()));
	}
	
	@PutMapping("/quotations/archive_all")
	@PreAuthorize("hasAnyAuthority('QUOTATION-CREATE','QUOTATION-UPDATE','QUOTATION-ARCHIVE')")
	public ResponseEntity<Boolean>archiveQuotations(){			
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/quotations/archive_all").toUriString());
		return ResponseEntity.created(uri).body(quotationService.archiveAll());
	}
	
	@PostMapping("/quotation_details/save")
	@PreAuthorize("hasAnyAuthority('QUOTATION-CREATE','QUOTATION-UPDATE')")
	public ResponseEntity<QuotationDetailModel>createQuotationDetail(
			@RequestBody QuotationDetail quotationDetail){
		if(quotationDetail.getQty() <= 0) {
			throw new InvalidEntryException("Quantity value should be more than 0");
		}
		Optional<Quotation> l = quotationRepository.findById(quotationDetail.getQuotation().getId());
		if(!l.isPresent()) {
			throw new NotFoundException("QUOTATION not found");
		}
		if(l.get().getStatus().equals("BLANK")) {
			l.get().setStatus("PENDING");
			quotationRepository.saveAndFlush(l.get());
		}
		if(!(l.get().getStatus().equals("PENDING") || l.get().getStatus().equals("BLANK"))) {
			throw new InvalidOperationException("Editing is not allowed, only PENDING or BLANK QUOTATIONs can be edited.");
		}
		Optional<Product> p = productRepository.findById(quotationDetail.getProduct().getId());
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		Optional<QuotationDetail> d = quotationDetailRepository.findByQuotationAndProduct(l.get(), p.get());
		QuotationDetail detail = new QuotationDetail();
		if(d.isPresent()) {
			/**
			 * Update existing detail
			 */
			detail = d.get();
			detail.setQty(quotationDetail.getQty());
			detail.setSellingPriceVatIncl(quotationDetail.getSellingPriceVatIncl());
			detail.setSellingPriceVatExcl(quotationDetail.getSellingPriceVatExcl());			
		}else {
			/**
			 * Create new detail
			 */
			detail.setQuotation(l.get());
			detail.setProduct(quotationDetail.getProduct());
			detail.setQty(quotationDetail.getQty());
			detail.setSellingPriceVatIncl(quotationDetail.getSellingPriceVatIncl());
			detail.setSellingPriceVatExcl(quotationDetail.getSellingPriceVatExcl());
		}		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/quotation_details/save").toUriString());
		return ResponseEntity.created(uri).body(quotationService.saveDetail(detail));
	}
	
	@GetMapping("/quotation_details/get")
	@PreAuthorize("hasAnyAuthority('QUOTATION-CREATE','QUOTATION-UPDATE')")
	public ResponseEntity<QuotationDetailModel>getDetail(
			@RequestParam(name = "id") Long id){		
		Optional<QuotationDetail> d = quotationDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}		
		QuotationDetailModel detail = new QuotationDetailModel();
		detail.setSellingPriceVatIncl(d.get().getSellingPriceVatIncl());
		detail.setSellingPriceVatExcl(d.get().getSellingPriceVatExcl());
		detail.setQty(d.get().getQty());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/quotation_details/get").toUriString());
		return ResponseEntity.created(uri).body(detail);
	}
	
	@DeleteMapping("/quotation_details/delete")
	@PreAuthorize("hasAnyAuthority('QUOTATION-CREATE','QUOTATION-UPDATE')")
	public ResponseEntity<Boolean> deleteDetail(
			@RequestParam(name = "id") Long id){		
		Optional<QuotationDetail> d = quotationDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}
		Quotation quotation = d.get().getQuotation();
		if(!quotation.getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only pending QUOTATION can be edited");
		}		
		quotationDetailRepository.delete(d.get());		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/quotation_details/delete").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
}
