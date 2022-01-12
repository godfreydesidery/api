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

import com.orbix.api.domain.SalesInvoice;
import com.orbix.api.domain.SalesInvoiceDetail;
import com.orbix.api.domain.Customer;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.Supplier;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.SalesInvoiceDetailModel;
import com.orbix.api.models.SalesInvoiceModel;
import com.orbix.api.repositories.CustomerRepository;
import com.orbix.api.repositories.SalesInvoiceDetailRepository;
import com.orbix.api.repositories.SalesInvoiceRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.SalesInvoiceDetailRepository;
import com.orbix.api.repositories.SalesInvoiceRepository;
import com.orbix.api.repositories.SupplierRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.SalesInvoiceService;
import com.orbix.api.service.SalesInvoiceService;
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
public class SalesInvoiceResource {
	
	private final 	UserService userService;
	private final 	DayService dayService;
	private final 	SalesInvoiceService salesInvoiceService;
	private final 	SalesInvoiceRepository salesInvoiceRepository;
	private final 	SalesInvoiceDetailRepository salesInvoiceDetailRepository;
	private final 	CustomerRepository customerRepository;
	private final 	ProductRepository productRepository;
	
	@GetMapping("/sales_invoices")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-READ')")
	public ResponseEntity<List<SalesInvoiceModel>>getSalesInvoices(){
		return ResponseEntity.ok().body(salesInvoiceService.getAllVisible());
	}
	
	@GetMapping("/sales_invoices/customer")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-READ')")
	public ResponseEntity<List<SalesInvoiceModel>>getSalesInvoices(
			@RequestParam(name = "id") Long id){
		Optional<Customer> c = customerRepository.findById(id);
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found in database");
		}
		return ResponseEntity.ok().body(salesInvoiceService.getByCustomerAndApprovedOrPartial(c.get()));
	}
	
	@GetMapping("/sales_invoices/get")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-READ')")
	public ResponseEntity<SalesInvoiceModel> getSalesInvoice(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(salesInvoiceService.get(id));
	}
	
	@GetMapping("/sales_invoices/get_by_no")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-READ')")
	public ResponseEntity<SalesInvoiceModel> getSalesInvoiceByNo(
			@RequestParam(name = "no") String no){
		return ResponseEntity.ok().body(salesInvoiceService.getByNo(no));
	}
	
	@GetMapping("/sales_invoice_details/get_by_salesInvoice")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-READ')")
	public ResponseEntity<List<SalesInvoiceDetailModel>>getSalesInvoiceDetails(
			@RequestParam(name = "id") Long id){		
		return ResponseEntity.ok().body(salesInvoiceService.getAllDetails(salesInvoiceRepository.findById(id).get()));
	}
	
	@PostMapping("/sales_invoices/create")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-CREATE')")
	public ResponseEntity<SalesInvoiceModel>createSalesInvoice(
			@RequestBody SalesInvoice salesInvoice,
			HttpServletRequest request){
		Optional<Customer> c = customerRepository.findByNo(salesInvoice.getCustomer().getNo());
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found");
		}
		SalesInvoice inv = new SalesInvoice();
		inv.setNo("NA");
		inv.setCustomer(c.get());
		inv.setStatus("BLANK");
		inv.setInvoiceDate(salesInvoice.getInvoiceDate());
		inv.setComments(salesInvoice.getComments());	
		inv.setCreatedBy(userService.getUserId(request));
		inv.setCreatedAt(dayService.getDayId());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_invoices/create").toUriString());
		return ResponseEntity.created(uri).body(salesInvoiceService.save(inv));
	}
	
	@PutMapping("/sales_invoices/update")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-UPDATE')")
	public ResponseEntity<SalesInvoiceModel>updateSalesInvoice(
			@RequestBody SalesInvoice salesInvoice,
			HttpServletRequest request){
		Optional<Customer> c = customerRepository.findByNo(salesInvoice.getCustomer().getNo());
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found");
		}
		Optional<SalesInvoice> l = salesInvoiceRepository.findById(salesInvoice.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("SALES_INVOICE not found");
		}
		if(!l.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only Pending Sales Invoices can be edited");
		}
		List<SalesInvoiceDetail> d = salesInvoiceDetailRepository.findBySalesInvoice(l.get());
		int i = 0;
		for(@SuppressWarnings("unused") SalesInvoiceDetail dt : d) {
			i = 1;
			break;
		}
		if(i > 0 && !l.get().getCustomer().equals(c.get())) {
			throw new InvalidOperationException("Changing Customer is not allowed for non blank Sales Invoices");
		}		
		l.get().setCustomer(c.get());
		l.get().setInvoiceDate(salesInvoice.getInvoiceDate());
		l.get().setComments(salesInvoice.getComments());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_invoices/update").toUriString());
		return ResponseEntity.created(uri).body(salesInvoiceService.save(l.get()));
	}
	
	@PutMapping("/sales_invoices/approve")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-APPROVE')")
	public ResponseEntity<SalesInvoiceModel>approveSalesInvoice(
			@RequestBody SalesInvoice salesInvoice,
			HttpServletRequest request){		
		Optional<SalesInvoice> l = salesInvoiceRepository.findById(salesInvoice.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("SALES_INVOICE not found");
		}
		if(l.get().getStatus().equals("PENDING")) {
			l.get().setApprovedBy(userService.getUserId(request));
			l.get().setApprovedAt(dayService.getDayId());
			l.get().setStatus("APPROVED");
		}else {
			throw new InvalidOperationException("Could not approve, not a PENDING SALES_INVOICE");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_invoices/approve").toUriString());
		return ResponseEntity.created(uri).body(salesInvoiceService.post(l.get()));
	}
	
	@PutMapping("/sales_invoices/cancel")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-CANCEL')")
	public ResponseEntity<SalesInvoiceModel>cancelSalesInvoice(
			@RequestBody SalesInvoice salesInvoice,
			HttpServletRequest request){		
		Optional<SalesInvoice> l = salesInvoiceRepository.findById(salesInvoice.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("SALES_INVOICE not found");
		}
		if(l.get().getStatus().equals("PENDING") || l.get().getStatus().equals("BLANK")) {
			l.get().setStatus("CANCELED");
		}else {
			throw new InvalidOperationException("Could not cancel, only Pending or Approved SALES_INVOICEs can be canceled");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_invoices/cancel").toUriString());
		return ResponseEntity.created(uri).body(salesInvoiceService.save(l.get()));
	}
	
	@PutMapping("/sales_invoices/archive")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-CREATE','SALES_INVOICE-UPDATE','SALES_INVOICE-ARCHIVE')")
	public ResponseEntity<Boolean>archiveSalesInvoice(
			@RequestBody SalesInvoice salesInvoice,
			HttpServletRequest request){		
		Optional<SalesInvoice> l = salesInvoiceRepository.findById(salesInvoice.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("SALES_INVOICE not found");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_invoices/archive").toUriString());
		return ResponseEntity.created(uri).body(salesInvoiceService.archive(l.get()));
	}
	
	@PutMapping("/sales_invoices/archive_all")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-CREATE','SALES_INVOICE-UPDATE','SALES_INVOICE-ARCHIVE')")
	public ResponseEntity<Boolean>archiveSalesInvoices(){			
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_invoices/archive_all").toUriString());
		return ResponseEntity.created(uri).body(salesInvoiceService.archiveAll());
	}
	
	@PostMapping("/sales_invoice_details/save")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-CREATE','SALES_INVOICE-UPDATE')")
	public ResponseEntity<SalesInvoiceDetailModel>createSalesInvoiceDetail(
			@RequestBody SalesInvoiceDetail salesInvoiceDetail){
		if(salesInvoiceDetail.getQty() <= 0) {
			throw new InvalidEntryException("Quantity value should be more than 0");
		}
		Optional<SalesInvoice> l = salesInvoiceRepository.findById(salesInvoiceDetail.getSalesInvoice().getId());
		if(!l.isPresent()) {
			throw new NotFoundException("SALES_INVOICE not found");
		}
		if(l.get().getStatus().equals("BLANK")) {
			l.get().setStatus("PENDING");
			salesInvoiceRepository.saveAndFlush(l.get());
		}
		if(!(l.get().getStatus().equals("PENDING") || l.get().getStatus().equals("BLANK"))) {
			throw new InvalidOperationException("Editing is not allowed, only PENDING or BLANK SALES_INVOICEs can be edited.");
		}
		Optional<Product> p = productRepository.findById(salesInvoiceDetail.getProduct().getId());
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		Optional<SalesInvoiceDetail> d = salesInvoiceDetailRepository.findBySalesInvoiceAndProduct(l.get(), p.get());
		SalesInvoiceDetail detail = new SalesInvoiceDetail();
		if(d.isPresent()) {
			/**
			 * Update existing detail
			 */
			detail = d.get();
			detail.setQty(salesInvoiceDetail.getQty());
			detail.setSellingPriceVatIncl(salesInvoiceDetail.getSellingPriceVatIncl());
			detail.setSellingPriceVatExcl(salesInvoiceDetail.getSellingPriceVatExcl());			
		}else {
			/**
			 * Create new detail
			 */
			detail.setSalesInvoice(l.get());
			detail.setProduct(salesInvoiceDetail.getProduct());
			detail.setQty(salesInvoiceDetail.getQty());
			detail.setSellingPriceVatIncl(salesInvoiceDetail.getSellingPriceVatIncl());
			detail.setSellingPriceVatExcl(salesInvoiceDetail.getSellingPriceVatExcl());
		}		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_invoice_details/save").toUriString());
		return ResponseEntity.created(uri).body(salesInvoiceService.saveDetail(detail));
	}
	
	@GetMapping("/sales_invoice_details/get")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-CREATE','SALES_INVOICE-UPDATE')")
	public ResponseEntity<SalesInvoiceDetailModel>getDetail(
			@RequestParam(name = "id") Long id){		
		Optional<SalesInvoiceDetail> d = salesInvoiceDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}		
		SalesInvoiceDetailModel detail = new SalesInvoiceDetailModel();
		detail.setSellingPriceVatIncl(d.get().getSellingPriceVatIncl());
		detail.setSellingPriceVatExcl(d.get().getSellingPriceVatExcl());
		detail.setQty(d.get().getQty());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_invoice_details/get").toUriString());
		return ResponseEntity.created(uri).body(detail);
	}
	
	@DeleteMapping("/sales_invoice_details/delete")
	@PreAuthorize("hasAnyAuthority('SALES_INVOICE-CREATE','SALES_INVOICE-UPDATE')")
	public ResponseEntity<Boolean> deleteDetail(
			@RequestParam(name = "id") Long id){		
		Optional<SalesInvoiceDetail> d = salesInvoiceDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}
		SalesInvoice salesInvoice = d.get().getSalesInvoice();
		if(!salesInvoice.getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only pending SALES_INVOICE can be edited");
		}		
		salesInvoiceDetailRepository.delete(d.get());		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_invoice_details/delete").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
}
