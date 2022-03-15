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
import com.orbix.api.domain.Product;
import com.orbix.api.domain.PackingList;
import com.orbix.api.domain.PackingListDetail;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.PackingListDetailModel;
import com.orbix.api.models.PackingListModel;
import com.orbix.api.repositories.CustomerRepository;
import com.orbix.api.repositories.EmployeeRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.PackingListDetailRepository;
import com.orbix.api.repositories.PackingListRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.ProductService;
import com.orbix.api.service.PackingListService;
import com.orbix.api.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PackingListResource {
	
	private final 	UserService userService;
	private final 	DayService dayService;
	private final 	PackingListService packingListService;
	private final 	PackingListRepository packingListRepository;
	private final 	PackingListDetailRepository packingListDetailRepository;
	private final 	CustomerRepository customerRepository;
	private final 	EmployeeRepository employeeRepository;
	private final 	ProductRepository productRepository;
	
	@GetMapping("/packing_lists")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-READ')")
	public ResponseEntity<List<PackingListModel>>getPackingLists(){
		return ResponseEntity.ok().body(packingListService.getAllVisible());
	}
	
	@GetMapping("/packing_lists/get")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-READ')")
	public ResponseEntity<PackingListModel> getPackingList(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(packingListService.get(id));
	}
	
	@GetMapping("/packing_lists/get_by_no")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-READ')")
	public ResponseEntity<PackingListModel> getPackingListByNo(
			@RequestParam(name = "no") String no){
		return ResponseEntity.ok().body(packingListService.getByNo(no));
	}
	
	@GetMapping("/packing_list_details/get_by_packingList")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-READ')")
	public ResponseEntity<List<PackingListDetailModel>>getPackingListDetails(
			@RequestParam(name = "id") Long id){		
		return ResponseEntity.ok().body(packingListService.getAllDetails(packingListRepository.findById(id).get()));
	}
	
	@PostMapping("/packing_lists/create")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CREATE')")
	public ResponseEntity<PackingListModel>createPackingList(
			@RequestBody PackingList packingList,
			HttpServletRequest request){
		Optional<Customer> c = customerRepository.findByNo(packingList.getCustomer().getNo());
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found");
		}
		Optional<Employee> e = employeeRepository.findByNo(packingList.getEmployee().getNo());
		if(!e.isPresent()) {
			throw new NotFoundException("Employee not found");
		}
		PackingList inv = new PackingList();
		inv.setNo("NA");
		inv.setCustomer(c.get());
		inv.setEmployee(e.get());
		inv.setStatus("PENDING");
		inv.setComments(packingList.getComments());	
		inv.setCreatedBy(userService.getUserId(request));
		inv.setCreatedAt(dayService.getDayId());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/packing_lists/create").toUriString());
		return ResponseEntity.created(uri).body(packingListService.save(inv));
	}
	
	@PutMapping("/packing_lists/update")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-UPDATE')")
	public ResponseEntity<PackingListModel>updatePackingList(
			@RequestBody PackingList packingList,
			HttpServletRequest request){
		Optional<Customer> c = customerRepository.findByNo(packingList.getCustomer().getNo());
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found");
		}
		Optional<Employee> e = employeeRepository.findByNo(packingList.getEmployee().getNo());
		if(!e.isPresent()) {
			throw new NotFoundException("Employee not found");
		}
		Optional<PackingList> l = packingListRepository.findById(packingList.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("PACKING_LIST not found");
		}
		if(!l.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only Pending Sales Issues can be edited");
		}
		List<PackingListDetail> d = packingListDetailRepository.findByPackingList(l.get());
		int i = 0;
		for(@SuppressWarnings("unused") PackingListDetail dt : d) {
			i = 1;
			break;
		}
		if(i > 0 && !l.get().getCustomer().equals(c.get())) {
			throw new InvalidOperationException("Changing Customer is not allowed for non blank Sales Issues");
		}		
		l.get().setCustomer(c.get());
		l.get().setEmployee(e.get());
		l.get().setComments(packingList.getComments());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/packing_lists/update").toUriString());
		return ResponseEntity.created(uri).body(packingListService.save(l.get()));
	}
	
	@PutMapping("/packing_lists/approve")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-APPROVE')")
	public ResponseEntity<PackingListModel>approvePackingList(
			@RequestBody PackingList packingList,
			HttpServletRequest request){		
		Optional<PackingList> l = packingListRepository.findById(packingList.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("PACKING_LIST not found");
		}
		if(l.get().getStatus().equals("PENDING")) {			
			l.get().setApprovedBy(userService.getUserId(request));
			l.get().setApprovedAt(dayService.getDayId());
			l.get().setStatus("APPROVED");
		}else {
			throw new InvalidOperationException("Could not approve, not a PENDING PACKING_LIST");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/packing_lists/approve").toUriString());
		return ResponseEntity.created(uri).body(packingListService.approve(l.get()));
	}
	
	@PutMapping("/packing_lists/cancel")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CANCEL')")
	public ResponseEntity<PackingListModel>cancelPackingList(
			@RequestBody PackingList packingList,
			HttpServletRequest request){		
		Optional<PackingList> l = packingListRepository.findById(packingList.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("PACKING_LIST not found");
		}
		if(l.get().getStatus().equals("PENDING") || l.get().getStatus().equals("BLANK")) {
			l.get().setStatus("CANCELED");
		}else {
			throw new InvalidOperationException("Could not cancel, only Pending or Approved PACKING_LISTs can be canceled");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/packing_lists/cancel").toUriString());
		return ResponseEntity.created(uri).body(packingListService.save(l.get()));
	}
	
	@PutMapping("/packing_lists/archive")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CREATE','PACKING_LIST-UPDATE','PACKING_LIST-ARCHIVE')")
	public ResponseEntity<Boolean>archivePackingList(
			@RequestBody PackingList packingList,
			HttpServletRequest request){		
		Optional<PackingList> l = packingListRepository.findById(packingList.getId());
		if(!l.isPresent()) {
			throw new NotFoundException("PACKING_LIST not found");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/packing_lists/archive").toUriString());
		return ResponseEntity.created(uri).body(packingListService.archive(l.get()));
	}
	
	@PutMapping("/packing_lists/archive_all")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CREATE','PACKING_LIST-UPDATE','PACKING_LIST-ARCHIVE')")
	public ResponseEntity<Boolean>archivePackingLists(){			
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/packing_lists/archive_all").toUriString());
		return ResponseEntity.created(uri).body(packingListService.archiveAll());
	}
	
	@PostMapping("/packing_list_details/save")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CREATE','PACKING_LIST-UPDATE')")
	public ResponseEntity<PackingListDetailModel>createPackingListDetail(
			@RequestBody PackingListDetail packingListDetail){
		
		if(packingListDetail.getPreviousReturns() < 0) {
			throw new InvalidEntryException("Previous returns must be positive");
		}
		if(packingListDetail.getQtyIssued() < 0) {
			throw new InvalidEntryException("Quantity issued must be positive");
		}
		if((packingListDetail.getTotalPacked() != packingListDetail.getPreviousReturns() + packingListDetail.getQtyIssued())) {
			throw new InvalidEntryException("Total packed quantity must be equal to returns and quantity issued");
		}				
		Optional<PackingList> l = packingListRepository.findById(packingListDetail.getPackingList().getId());
		if(!l.isPresent()) {
			throw new NotFoundException("PACKING_LIST not found");
		}		
		
		Optional<Product> p = productRepository.findById(packingListDetail.getProduct().getId());
		if(!p.isPresent()) {
			throw new NotFoundException("Product not found");
		}
		Optional<PackingListDetail> d = packingListDetailRepository.findByPackingListAndProduct(l.get(), p.get());
		PackingListDetail detail = new PackingListDetail();
		if(d.isPresent()) {
			/**
			 * Update existing detail
			 */
			detail = d.get();
			if((l.get().getStatus().equals("PENDING"))){
				if(packingListDetail.getTotalPacked() != packingListDetail.getPreviousReturns() + packingListDetail.getQtyIssued()) {
					throw new InvalidEntryException("Total quantity must be a sum of previous returns and qty issued");
				}
				detail.setPreviousReturns(packingListDetail.getPreviousReturns());
				detail.setQtyIssued(packingListDetail.getQtyIssued());
				detail.setTotalPacked(packingListDetail.getTotalPacked());
				detail.setCostPriceVatIncl(packingListDetail.getCostPriceVatIncl());
				detail.setCostPriceVatExcl(packingListDetail.getCostPriceVatExcl());
				detail.setSellingPriceVatIncl(packingListDetail.getSellingPriceVatIncl());
				detail.setSellingPriceVatExcl(packingListDetail.getSellingPriceVatExcl());
			}						
		}else {
			/**
			 * Create new detail
			 */
			detail.setPackingList(l.get());			
			detail.setProduct(packingListDetail.getProduct());
			detail.setPreviousReturns(packingListDetail.getPreviousReturns());
			detail.setQtyIssued(packingListDetail.getQtyIssued());
			detail.setTotalPacked(packingListDetail.getTotalPacked());
			detail.setCostPriceVatIncl(packingListDetail.getCostPriceVatIncl());
			detail.setCostPriceVatExcl(packingListDetail.getCostPriceVatExcl());
			detail.setSellingPriceVatIncl(packingListDetail.getSellingPriceVatIncl());
			detail.setSellingPriceVatExcl(packingListDetail.getSellingPriceVatExcl());
		}		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/packing_list_details/save").toUriString());
		return ResponseEntity.created(uri).body(packingListService.saveDetail(detail));
	}
	
	@GetMapping("/packing_list_details/get")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CREATE','PACKING_LIST-UPDATE')")
	public ResponseEntity<PackingListDetailModel>getDetail(
			@RequestParam(name = "id") Long id){		
		Optional<PackingListDetail> d = packingListDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}		
		PackingListDetailModel detail = new PackingListDetailModel();
		detail.setId(d.get().getId());
		detail.setProduct((d.get().getProduct()));
		detail.setPreviousReturns((d.get().getPreviousReturns()));
		detail.setQtyIssued((d.get().getQtyIssued()));
		detail.setTotalPacked((d.get().getTotalPacked()));
		detail.setSellingPriceVatIncl((d.get().getSellingPriceVatIncl()));
		detail.setSellingPriceVatExcl((d.get().getSellingPriceVatExcl()));
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/packing_list_details/get").toUriString());
		return ResponseEntity.created(uri).body(detail);
	}
	
	@DeleteMapping("/packing_list_details/delete")
	@PreAuthorize("hasAnyAuthority('PACKING_LIST-CREATE','PACKING_LIST-UPDATE')")
	public ResponseEntity<Boolean> deleteDetail(
			@RequestParam(name = "id") Long id){		
		Optional<PackingListDetail> d = packingListDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Detail not found");
		}
		PackingList packingList = d.get().getPackingList();
		if(!packingList.getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Editing not allowed, only pending PACKING_LIST can be edited");
		}		
		packingListDetailRepository.delete(d.get());		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/packing_list_details/delete").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
}
