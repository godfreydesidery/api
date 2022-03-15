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
import com.orbix.api.domain.Employee;
import com.orbix.api.domain.SalesList;
import com.orbix.api.domain.SalesListDetail;
import com.orbix.api.domain.Product;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.SalesListDetailModel;
import com.orbix.api.models.SalesListModel;
import com.orbix.api.repositories.CustomerRepository;
import com.orbix.api.repositories.EmployeeRepository;
import com.orbix.api.repositories.SalesListDetailRepository;
import com.orbix.api.repositories.SalesListRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.SalesListService;
import com.orbix.api.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SalesListResource {
	private final 	UserService userService;
	private final 	DayService dayService;
	private final 	SalesListService salesListService;
	private final 	SalesListRepository salesListRepository;
	private final 	SalesListDetailRepository salesListDetailRepository;
	private final 	CustomerRepository customerRepository;
	private final 	EmployeeRepository employeeRepository;
	private final 	ProductRepository productRepository;
	
	@GetMapping("/sales_lists")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-READ')")
	public ResponseEntity<List<SalesListModel>>getSalesLists(){
		return ResponseEntity.ok().body(salesListService.getAllVisible());
	}
	
	@GetMapping("/sales_lists/get")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-READ')")
	public ResponseEntity<SalesListModel> getSalesList(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(salesListService.get(id));
	}
	
	@GetMapping("/sales_lists/get_by_no")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-READ')")
	public ResponseEntity<SalesListModel> getSalesListByNo(
			@RequestParam(name = "no") String no){
		return ResponseEntity.ok().body(salesListService.getByNo(no));
	}
	
	@GetMapping("/sales_list_details/get_by_salesList")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-READ')")
	public ResponseEntity<List<SalesListDetailModel>>getSalesListDetails(
			@RequestParam(name = "id") Long id){		
		return ResponseEntity.ok().body(salesListService.getAllDetails(salesListRepository.findById(id).get()));
	}
	
	@PostMapping("/sales_lists/create")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CREATE')")
	public ResponseEntity<SalesListModel>createSalesList(
			@RequestBody SalesList salesList,
			HttpServletRequest request){
		Optional<Customer> c = customerRepository.findByNo(salesList.getCustomer().getNo());
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found");
		}
		Optional<Employee> e = employeeRepository.findByNo(salesList.getEmployee().getNo());
		if(!e.isPresent()) {
			throw new NotFoundException("Employee not found");
		}
		SalesList inv = new SalesList();
		inv.setNo("NA");
		inv.setCustomer(c.get());
		inv.setEmployee(e.get());
		inv.setStatus("PENDING");
		inv.setIssueDate(salesList.getIssueDate());
		inv.setComments(salesList.getComments());	
		inv.setCreatedBy(userService.getUserId(request));
		inv.setCreatedAt(dayService.getDayId());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_lists/create").toUriString());
		return ResponseEntity.created(uri).body(salesListService.save(inv));
	}
	
	@PutMapping("/sales_lists/update")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-UPDATE')")
	public ResponseEntity<SalesListModel>updateSalesList(
			@RequestBody SalesList salesList,
			HttpServletRequest request){
		Optional<Customer> c = customerRepository.findByNo(salesList.getCustomer().getNo());
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found");
		}
		Optional<Employee> e = employeeRepository.findByNo(salesList.getEmployee().getNo());
		if(!e.isPresent()) {
			throw new NotFoundException("Employee not found");
		}
		Optional<SalesList> l = salesListRepository.findById(salesList.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("PACKING_LIST not found");
		}
		if(!l.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only Pending Sales Issues can be edited");
		}
		List<SalesListDetail> d = salesListDetailRepository.findBySalesList(l.get());
		int i = 0;
		for(@SuppressWarnings("unused") SalesListDetail dt : d) {
			i = 1;
			break;
		}
		if(i > 0 && !l.get().getCustomer().equals(c.get())) {
			throw new InvalidOperationException("Changing Customer is not allowed for non blank Sales Issues");
		}		
		l.get().setCustomer(c.get());
		l.get().setEmployee(e.get());
		l.get().setIssueDate(salesList.getIssueDate());
		l.get().setComments(salesList.getComments());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_lists/update").toUriString());
		return ResponseEntity.created(uri).body(salesListService.save(l.get()));
	}
	
	@PutMapping("/sales_lists/approve")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-APPROVE')")
	public ResponseEntity<SalesListModel>postSalesList(
			@RequestBody SalesList salesList,
			HttpServletRequest request){		
		Optional<SalesList> l = salesListRepository.findById(salesList.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("PACKING_LIST not found");
		}
		if(l.get().getStatus().equals("PENDING")) {
			l.get().setTotalReturns(salesList.getTotalReturns());
			l.get().setTotalDamages(salesList.getTotalDamages());
			l.get().setTotalDeficit(salesList.getTotalDeficit());
			l.get().setTotalDiscounts(salesList.getTotalDiscounts());
			l.get().setTotalExpenditures(salesList.getTotalExpenditures());
			l.get().setTotalBank(salesList.getTotalBank());
			l.get().setTotalCash(salesList.getTotalCash());
			
			l.get().setApprovedBy(userService.getUserId(request));
			l.get().setApprovedAt(dayService.getDayId());
			l.get().setStatus("APPROVED");
		}else {
			throw new InvalidOperationException("Could not approve, only PENDING Sales List can be approved");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_lists/approve").toUriString());
		return ResponseEntity.created(uri).body(salesListService.approve(l.get(), request));
	}
	
	@PutMapping("/sales_lists/cancel")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CANCEL')")
	public ResponseEntity<SalesListModel>cancelSalesList(
			@RequestBody SalesList salesList,
			HttpServletRequest request){		
		Optional<SalesList> l = salesListRepository.findById(salesList.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("PACKING_LIST not found");
		}
		if(l.get().getStatus().equals("PENDING") || l.get().getStatus().equals("BLANK")) {
			l.get().setStatus("CANCELED");
		}else {
			throw new InvalidOperationException("Could not cancel, only Pending or Approved PACKING_LISTs can be canceled");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_lists/cancel").toUriString());
		return ResponseEntity.created(uri).body(salesListService.save(l.get()));
	}
	
	@PutMapping("/sales_lists/archive")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CREATE','PACKING_LIST-UPDATE','PACKING_LIST-ARCHIVE')")
	public ResponseEntity<Boolean>archiveSalesList(
			@RequestBody SalesList salesList,
			HttpServletRequest request){		
		Optional<SalesList> l = salesListRepository.findById(salesList.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("PACKING_LIST not found");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_lists/archive").toUriString());
		return ResponseEntity.created(uri).body(salesListService.archive(l.get()));
	}
	
	@PutMapping("/sales_lists/archive_all")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CREATE','PACKING_LIST-UPDATE','PACKING_LIST-ARCHIVE')")
	public ResponseEntity<Boolean>archiveSalesLists(){			
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_lists/archive_all").toUriString());
		return ResponseEntity.created(uri).body(salesListService.archiveAll());
	}
	
	@PostMapping("/sales_list_details/save")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CREATE','PACKING_LIST-UPDATE')")
	public ResponseEntity<SalesListDetailModel>createSalesListDetail(
			@RequestBody SalesListDetail salesListDetail){
		
		if(salesListDetail.getQtySold() < 0) {
			throw new InvalidEntryException("Quantity sold must be positive");
		}
		if(salesListDetail.getQtyOffered() < 0) {
			throw new InvalidEntryException("Quantity offered must be positive");
		}
		if(salesListDetail.getQtyReturned() < 0) {
			throw new InvalidEntryException("Quantity returned must be positive");
		}
		if(salesListDetail.getQtyDamaged() < 0) {
			throw new InvalidEntryException("Quantity damaged must be positive");
		}					
		Optional<SalesList> l = salesListRepository.findById(salesListDetail.getSalesList().getId());
		if(!l.isPresent()) {
			throw new NotFoundException("PACKING_LIST not found");
		}		
		
		Optional<Product> p = productRepository.findById(salesListDetail.getProduct().getId());
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		Optional<SalesListDetail> d = salesListDetailRepository.findBySalesListAndProduct(l.get(), p.get());
		SalesListDetail detail = new SalesListDetail();
		if(d.isPresent()) {
			/**
			 * Update existing detail
			 */
			detail = d.get();
			
			if(l.get().getStatus().equals("PENDING")) {
				if(salesListDetail.getTotalPacked() != salesListDetail.getQtySold() + salesListDetail.getQtyOffered() + salesListDetail.getQtyReturned() + salesListDetail.getQtyDamaged()) {
					throw new InvalidEntryException("Total quantity must be a sum of qty sold, qty offered, qty returned and qty damaged");
				}
				detail.setQtySold(salesListDetail.getQtySold());
				detail.setQtyOffered(salesListDetail.getQtyOffered());
				detail.setQtyReturned(salesListDetail.getQtyReturned());
				detail.setQtyDamaged(salesListDetail.getQtyDamaged());
			}
			
		}else {
			/**
			 * Throw exception
			 */
			throw new NotFoundException("Detail not found");
		}		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_list_details/save").toUriString());
		return ResponseEntity.created(uri).body(salesListService.saveDetail(detail));
	}
	
	@GetMapping("/sales_list_details/get")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CREATE','PACKING_LIST-UPDATE')")
	public ResponseEntity<SalesListDetailModel>getDetail(
			@RequestParam(name = "id") Long id){		
		Optional<SalesListDetail> d = salesListDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}		
		SalesListDetailModel detail = new SalesListDetailModel();
		detail.setId(d.get().getId());
		detail.setProduct((d.get().getProduct()));
		detail.setTotalPacked((d.get().getTotalPacked()));
		detail.setSellingPriceVatIncl((d.get().getSellingPriceVatIncl()));
		detail.setSellingPriceVatExcl((d.get().getSellingPriceVatExcl()));
		detail.setQtySold((d.get().getQtySold()));
		detail.setQtyOffered((d.get().getQtyOffered()));
		detail.setQtyReturned((d.get().getQtyReturned()));
		detail.setQtyDamaged((d.get().getQtyDamaged()));
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_list_details/get").toUriString());
		return ResponseEntity.created(uri).body(detail);
	}
	
	@DeleteMapping("/sales_list_details/delete")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CREATE','PACKING_LIST-UPDATE')")
	public ResponseEntity<Boolean> deleteDetail(
			@RequestParam(name = "id") Long id){		
		Optional<SalesListDetail> d = salesListDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}
		SalesList salesList = d.get().getSalesList();
		if(!salesList.getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only pending PACKING_LIST can be edited");
		}		
		salesListDetailRepository.delete(d.get());		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sales_list_details/delete").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
}
