/**
 * 
 */
package com.orbix.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Debt;
import com.orbix.api.domain.PackingList;
import com.orbix.api.domain.PackingListDetail;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductDamage;
import com.orbix.api.domain.ProductOffer;
import com.orbix.api.domain.Sale;
import com.orbix.api.domain.SaleDetail;
import com.orbix.api.domain.ProductStockCard;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.PackingListDetailModel;
import com.orbix.api.models.PackingListModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.PackingListDetailRepository;
import com.orbix.api.repositories.PackingListRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.SaleDetailRepository;
import com.orbix.api.repositories.SaleRepository;
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
public class PackingListServiceImpl implements PackingListService {
	
	private final PackingListRepository packingListRepository;
	private final PackingListDetailRepository packingListDetailRepository;
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final ProductRepository productRepository;
	private final ProductStockCardService productStockCardService;
	private final SaleService saleService;
	private final SaleRepository saleRepository;
	private final SaleDetailRepository saleDetailRepository;
	private final ProductDamageService productDamageService;
	private final ProductOfferService productOfferService;
	private final DebtService debtService;

	@Override
	public PackingListModel save(PackingList packingList) {
		if(!validate(packingList)) {
			throw new InvalidEntryException("Could not save, Sales Issue pclalid");
		}
		PackingList pcl = packingListRepository.saveAndFlush(packingList);
		if(pcl.getNo().equals("NA")) {
			pcl.setNo(generatePackingListNo(pcl));
			pcl = packingListRepository.save(pcl);
		}
		
		double totalPreviousReturns = 0;
		double totalAmountIssued = 0;
		double totalAmountPacked = 0;
		double totalSales = 0;
		double totalReturns = 0;
		double totalOffered = 0;
		double totalDamages = 0;
		double totalDiscounts = pcl.getTotalDiscounts();
		double totalExpenditures = pcl.getTotalExpenditures();
		double totalBank = pcl.getTotalBank();
		double totalCash = pcl.getTotalCash();
		double totalDeficit = pcl.getTotalDeficit();
		
		PackingListModel model = new PackingListModel();
		model.setId(pcl.getId());
		model.setNo(pcl.getNo());
		model.setCustomer(pcl.getCustomer());
		model.setEmployee(pcl.getEmployee());
		model.setStatus(pcl.getStatus());
		model.setIssueDate(pcl.getIssueDate());		
		model.setComments(pcl.getComments());
		model.setTotalBank(pcl.getTotalBank());
		model.setTotalCash(pcl.getTotalCash());
		model.setTotalDamages(pcl.getTotalDamages());
		model.setTotalDeficit(pcl.getTotalDeficit());
		model.setTotalDiscounts(pcl.getTotalDiscounts());
		model.setTotalExpenditures(pcl.getTotalReturns());
		model.setTotalReturns(pcl.getTotalReturns());
		if(pcl.getCreatedAt() != null && pcl.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(pcl.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.getCreatedBy()));
		}
		if(pcl.getApprovedAt() != null && pcl.getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(pcl.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.getApprovedBy()));
		}
		if(pcl.getPostedAt() != null && pcl.getPostedBy() != null) {
			model.setPosted(dayRepository.findById(pcl.getPostedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.getPostedBy()));
		}
		List<PackingListDetailModel> details = new ArrayList<PackingListDetailModel>();
		List<PackingListDetail> ds = packingListDetailRepository.findByPackingList(pcl);
		if(!ds.isEmpty()) {
			for(PackingListDetail d : ds) {
				PackingListDetailModel detail = new PackingListDetailModel();
				detail.setId(d.getId());
				detail.setPreviousReturns(d.getPreviousReturns());
				detail.setProduct(d.getProduct());
				detail.setQtyDamaged(d.getQtyDamaged());
				detail.setQtyIssued(d.getQtyIssued());
				detail.setTotalPacked(d.getTotalPacked());
				detail.setQtyOffered(d.getQtyOffered());
				detail.setQtyReturned(d.getQtyReturned());
				detail.setQtySold(d.getQtySold());
				detail.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
				detail.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
				details.add(detail);
				
				totalPreviousReturns = totalPreviousReturns + d.getPreviousReturns() * d.getSellingPriceVatIncl();
				totalAmountIssued = totalAmountIssued + d.getQtyIssued() * d.getSellingPriceVatIncl();
				totalAmountPacked = totalAmountPacked + d.getTotalPacked() * d.getSellingPriceVatIncl();
				totalSales = totalSales + d.getQtySold() * d.getSellingPriceVatIncl();
				totalReturns = totalReturns + d.getQtyReturned() * d.getSellingPriceVatIncl();
				totalOffered = totalOffered + d.getQtyOffered() * d.getSellingPriceVatIncl();
				totalDamages = totalDamages + d.getQtyDamaged() * d.getSellingPriceVatIncl();	
			}
			model.setPackingListDetails(details);
			
			model.setTotalPreviousReturns(totalPreviousReturns);
			model.setTotalAmountIssued(totalAmountIssued);
			model.setTotalAmountPacked(totalAmountPacked);
			model.setTotalSales(totalSales);
			model.setTotalOffered(totalOffered);
			model.setTotalReturns(totalReturns);
			model.setTotalDamages(totalDamages);
			
			model.setTotalDiscounts(totalDiscounts);
			model.setTotalExpenditures(totalExpenditures);
			model.setTotalBank(totalBank);
			model.setTotalCash(totalCash);
			model.setTotalDeficit(totalDeficit);
		}
		
		return model;
	}

	@Override
	public PackingListModel get(Long id) {
		PackingListModel model = new PackingListModel();
		Optional<PackingList> pcl = packingListRepository.findById(id);
		if(!pcl.isPresent()) {
			throw new NotFoundException("Packing List not found");
		}
		model.setId(pcl.get().getId());
		model.setNo(pcl.get().getNo());
		model.setCustomer(pcl.get().getCustomer());
		model.setEmployee(pcl.get().getEmployee());
		model.setStatus(pcl.get().getStatus());
		model.setIssueDate(pcl.get().getIssueDate());		
		model.setComments(pcl.get().getComments());
		
		double totalPreviousReturns = 0;
		double totalAmountIssued = 0;
		double totalAmountPacked = 0;
		double totalSales = 0;
		double totalReturns = 0;
		double totalOffered = 0;
		double totalDamages = 0;
		double totalDiscounts = pcl.get().getTotalDiscounts();
		double totalExpenditures = pcl.get().getTotalExpenditures();
		double totalBank = pcl.get().getTotalBank();
		double totalCash = pcl.get().getTotalCash();
		double totalDeficit = pcl.get().getTotalDeficit();
		
		if(pcl.get().getCreatedAt() != null && pcl.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(pcl.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.get().getCreatedBy()));
		}
		if(pcl.get().getApprovedAt() != null && pcl.get().getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(pcl.get().getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.get().getApprovedBy()));
		}
		if(pcl.get().getPostedAt() != null && pcl.get().getPostedBy() != null) {
			model.setPosted(dayRepository.findById(pcl.get().getPostedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.get().getPostedBy()));
		}
		List<PackingListDetail> packingListDetails = pcl.get().getPackingListDetails();
		List<PackingListDetailModel> details = new ArrayList<PackingListDetailModel>();
		for(PackingListDetail d : packingListDetails) {
			PackingListDetailModel detail = new PackingListDetailModel();
			detail.setId(d.getId());
			detail.setPreviousReturns(d.getPreviousReturns());
			detail.setProduct(d.getProduct());
			detail.setQtyDamaged(d.getQtyDamaged());
			detail.setQtyIssued(d.getQtyIssued());
			detail.setTotalPacked(d.getTotalPacked());
			detail.setQtyOffered(d.getQtyOffered());
			detail.setQtyReturned(d.getQtyReturned());
			detail.setQtySold(d.getQtySold());
			detail.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
			detail.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
			details.add(detail);
			
			totalPreviousReturns = totalPreviousReturns + d.getPreviousReturns() * d.getSellingPriceVatIncl();
			totalAmountIssued = totalAmountIssued + d.getQtyIssued() * d.getSellingPriceVatIncl();
			totalAmountPacked = totalAmountPacked + d.getTotalPacked() * d.getSellingPriceVatIncl();
			totalSales = totalSales + d.getQtySold() * d.getSellingPriceVatIncl();
			totalReturns = totalReturns + d.getQtyReturned() * d.getSellingPriceVatIncl();
			totalOffered = totalOffered + d.getQtyOffered() * d.getSellingPriceVatIncl();
			totalDamages = totalDamages + d.getQtyDamaged() * d.getSellingPriceVatIncl();	
		}
		model.setPackingListDetails(details);
		
		model.setTotalPreviousReturns(totalPreviousReturns);
		model.setTotalAmountIssued(totalAmountIssued);
		model.setTotalAmountPacked(totalAmountPacked);
		model.setTotalSales(totalSales);
		model.setTotalOffered(totalOffered);
		model.setTotalReturns(totalReturns);
		model.setTotalDamages(totalDamages);
		
		model.setTotalDiscounts(totalDiscounts);
		model.setTotalExpenditures(totalExpenditures);
		model.setTotalBank(totalBank);
		model.setTotalCash(totalCash);
		model.setTotalDeficit(totalDeficit);
		
		return model;
	}

	@Override
	public PackingListModel getByNo(String no) {
		PackingListModel model = new PackingListModel();
		Optional<PackingList> pcl = packingListRepository.findByNo(no);
		if(!pcl.isPresent()) {
			throw new NotFoundException("Packing List not found");
		}
		model.setId(pcl.get().getId());
		model.setNo(pcl.get().getNo());
		model.setCustomer(pcl.get().getCustomer());
		model.setEmployee(pcl.get().getEmployee());
		model.setStatus(pcl.get().getStatus());
		model.setIssueDate(pcl.get().getIssueDate());		
		model.setComments(pcl.get().getComments());
		
		double totalPreviousReturns = 0;
		double totalAmountIssued = 0;
		double totalAmountPacked = 0;
		double totalSales = 0;
		double totalReturns = 0;
		double totalOffered = 0;
		double totalDamages = 0;
		double totalDiscounts = pcl.get().getTotalDiscounts();
		double totalExpenditures = pcl.get().getTotalExpenditures();
		double totalBank = pcl.get().getTotalBank();
		double totalCash = pcl.get().getTotalCash();
		double totalDeficit = pcl.get().getTotalDeficit();
		
		if(pcl.get().getCreatedAt() != null && pcl.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(pcl.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.get().getCreatedBy()));
		}
		if(pcl.get().getApprovedAt() != null && pcl.get().getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(pcl.get().getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.get().getApprovedBy()));
		}	
		if(pcl.get().getPostedAt() != null && pcl.get().getPostedBy() != null) {
			model.setPosted(dayRepository.findById(pcl.get().getPostedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.get().getPostedBy()));
		}	
		List<PackingListDetail> packingListDetails = pcl.get().getPackingListDetails();
		List<PackingListDetailModel> details = new ArrayList<PackingListDetailModel>();
		for(PackingListDetail d : packingListDetails) {
			PackingListDetailModel detail = new PackingListDetailModel();
			detail.setId(d.getId());
			detail.setPreviousReturns(d.getPreviousReturns());
			detail.setProduct(d.getProduct());
			detail.setQtyDamaged(d.getQtyDamaged());
			detail.setQtyIssued(d.getQtyIssued());
			detail.setTotalPacked(d.getTotalPacked());
			detail.setQtyOffered(d.getQtyOffered());
			detail.setQtyReturned(d.getQtyReturned());
			detail.setQtySold(d.getQtySold());
			detail.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
			detail.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
			details.add(detail);
			
			totalPreviousReturns = totalPreviousReturns + d.getPreviousReturns() * d.getSellingPriceVatIncl();
			totalAmountIssued = totalAmountIssued + d.getQtyIssued() * d.getSellingPriceVatIncl();
			totalAmountPacked = totalAmountPacked + d.getTotalPacked() * d.getSellingPriceVatIncl();
			totalSales = totalSales + d.getQtySold() * d.getSellingPriceVatIncl();
			totalReturns = totalReturns + d.getQtyReturned() * d.getSellingPriceVatIncl();
			totalOffered = totalOffered + d.getQtyOffered() * d.getSellingPriceVatIncl();
			totalDamages = totalDamages + d.getQtyDamaged() * d.getSellingPriceVatIncl();
		}
		model.setPackingListDetails(details);
		
		model.setTotalPreviousReturns(totalPreviousReturns);
		model.setTotalAmountIssued(totalAmountIssued);
		model.setTotalAmountPacked(totalAmountPacked);
		model.setTotalSales(totalSales);
		model.setTotalOffered(totalOffered);
		model.setTotalReturns(totalReturns);
		model.setTotalDamages(totalDamages);
		
		model.setTotalDiscounts(totalDiscounts);
		model.setTotalExpenditures(totalExpenditures);
		model.setTotalBank(totalBank);
		model.setTotalCash(totalCash);
		model.setTotalDeficit(totalDeficit);
		
		return model;
	}

	@Override
	public boolean delete(PackingList packingList) {
		if(!allowDelete(packingList)) {
			throw new InvalidOperationException("Deleting the selected Sales Issue is not allowed");
		}
		packingListRepository.delete(packingList);
		return true;
	}

	@Override
	public List<PackingListModel> getAllVisible() {
		List<String> statuses = new ArrayList<String>();
		statuses.add("BLANK");
		statuses.add("PENDING");
		statuses.add("APPROVED");
		statuses.add("POSTED");
		List<PackingList> packingLists = packingListRepository.findAllVissible(statuses);
		List<PackingListModel> models = new ArrayList<PackingListModel>();
		for(PackingList pcl : packingLists) {
			PackingListModel model = new PackingListModel();
			model.setId(pcl.getId());
			model.setNo(pcl.getNo());
			model.setCustomer(pcl.getCustomer());
			model.setStatus(pcl.getStatus());
			model.setIssueDate(pcl.getIssueDate());		
			model.setComments(pcl.getComments());
			model.setTotalBank(pcl.getTotalBank());
			model.setTotalCash(pcl.getTotalCash());
			model.setTotalDamages(pcl.getTotalDamages());
			model.setTotalDeficit(pcl.getTotalDeficit());
			model.setTotalDiscounts(pcl.getTotalDiscounts());
			model.setTotalExpenditures(pcl.getTotalReturns());
			model.setTotalReturns(pcl.getTotalReturns());
			
			if(pcl.getCreatedAt() != null && pcl.getCreatedBy() != null) {
				model.setCreated(dayRepository.findById(pcl.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.getCreatedBy()));
			}
			if(pcl.getApprovedAt() != null && pcl.getApprovedBy() != null) {
				model.setApproved(dayRepository.findById(pcl.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.getApprovedBy()));
			}	
			if(pcl.getPostedAt() != null && pcl.getPostedBy() != null) {
				model.setPosted(dayRepository.findById(pcl.getPostedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.getPostedBy()));
			}				
			models.add(model);
		}
		return models;
	}

	@Override
	public PackingListDetailModel saveDetail(PackingListDetail packingListDetail) {
		if(!validateDetail(packingListDetail)) {
			throw new InvalidEntryException("Could not save detail, Invalid entry");
		}
		PackingListDetailModel detail = new PackingListDetailModel();
		PackingListDetail d = packingListDetailRepository.save(packingListDetail);
		detail.setId(d.getId());
		detail.setPreviousReturns(d.getPreviousReturns());
		detail.setProduct(d.getProduct());
		detail.setQtyDamaged(d.getQtyDamaged());
		detail.setQtyIssued(d.getQtyIssued());
		detail.setTotalPacked(d.getTotalPacked());
		detail.setQtyOffered(d.getQtyOffered());
		detail.setQtyReturned(d.getQtyReturned());
		detail.setQtySold(d.getQtySold());
		detail.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
		detail.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
		return detail;
	}

	@Override
	public PackingListDetailModel getDetail(Long id) {
		PackingListDetailModel detail = new PackingListDetailModel();
		Optional<PackingListDetail> d = packingListDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Packing List detail not found");
		}
		detail.setId(d.get().getId());
		detail.setPreviousReturns(d.get().getPreviousReturns());
		detail.setProduct(d.get().getProduct());
		detail.setQtyDamaged(d.get().getQtyDamaged());
		detail.setQtyIssued(d.get().getQtyIssued());
		detail.setTotalPacked(d.get().getTotalPacked());
		detail.setQtyOffered(d.get().getQtyOffered());
		detail.setQtyReturned(d.get().getQtyReturned());
		detail.setQtySold(d.get().getQtySold());
		detail.setSellingPriceVatIncl(d.get().getSellingPriceVatIncl());
		detail.setSellingPriceVatExcl(d.get().getSellingPriceVatExcl());
		return detail;
	}

	@Override
	public boolean deleteDetail(PackingListDetail packingListDetail) {
		if(!allowDeleteDetail(packingListDetail)) {
			throw new InvalidOperationException("Deleting the selected detail is not allowed");
		}
		packingListDetailRepository.delete(packingListDetail);
		return true;
	}

	@Override
	public List<PackingListDetailModel> getAllDetails(PackingList packingList) {
		List<PackingListDetail> details = packingListDetailRepository.findByPackingList(packingList);
		List<PackingListDetailModel> models = new ArrayList<PackingListDetailModel>();
		for(PackingListDetail detail : details) {
			PackingListDetailModel model = new PackingListDetailModel();
			model.setId(detail.getId());
			model.setPreviousReturns(detail.getPreviousReturns());
			model.setProduct(detail.getProduct());
			model.setQtyDamaged(detail.getQtyDamaged());
			model.setQtyIssued(detail.getQtyIssued());
			model.setTotalPacked(detail.getTotalPacked());
			model.setQtyOffered(detail.getQtyOffered());
			model.setQtyReturned(detail.getQtyReturned());
			model.setQtySold(detail.getQtySold());
			model.setSellingPriceVatIncl(detail.getSellingPriceVatIncl());
			model.setSellingPriceVatExcl(detail.getSellingPriceVatExcl());
			models.add(model);
		}
		return models;	
	}

	@Override
	public boolean archive(PackingList packingList) {
		if(!packingList.getStatus().equals("POSTED")) {
			throw new InvalidOperationException("Could not process, only a posted packing list can be archived");
		}
		if(packingList.getTotalDeficit() > 0) {
			throw new InvalidOperationException("Could not process, non debt free document can not be archived");
		}
		packingList.setStatus("ARCHIVED");
		packingListRepository.saveAndFlush(packingList);
		return true;
	}

	@Override
	public boolean archiveAll() {
		List<PackingList> packingLists = packingListRepository.findAllPosted("POSTED");
		if(packingLists.isEmpty()) {
			throw new NotFoundException("No Packing List to archive");
		}
		for(PackingList p : packingLists) {	
			if(!(p.getTotalDeficit() > 0)) {
				p.setStatus("ARCHIVED");
				packingListRepository.saveAndFlush(p);
			}		
		}
		return true;
	}
	
	@Override
	public PackingListModel print(PackingList packingList) {
		PackingList pcl = packingListRepository.saveAndFlush(packingList);
		List<PackingListDetail> details = pcl.getPackingListDetails();
		double totalPreviousReturns = 0;
		double totalAmountIssued = 0;
		double totalAmountPacked = 0;
		double totalSales = 0;
		double totalReturns = 0;
		double totalOffered = 0;
		double totalDamages = 0;
		double totalDiscounts = pcl.getTotalDiscounts();
		double totalExpenditures = pcl.getTotalExpenditures();
		double totalBank = pcl.getTotalBank();
		double totalCash = pcl.getTotalCash();
		double totalDeficit = pcl.getTotalDeficit();
		if(totalDiscounts != 0) {
			throw new InvalidEntryException("Invalid entry in discount amount");
		}
		if(totalExpenditures != 0) {
			throw new InvalidEntryException("Invalid entry in expenditure amount");
		}
		if(totalBank != 0) {
			throw new InvalidEntryException("Invalid entry in amount to bank");
		}
		if(totalCash != 0) {
			throw new InvalidEntryException("Invalid entry in cash amount");
		}
		if(totalDeficit != 0) {
			throw new InvalidEntryException("Invalid entry in deficit amount");
		}
		for(PackingListDetail d : details) {				
			/**
			 * Grab stock qty and update stock
			 */
			Product product =productRepository.findById(d.getProduct().getId()).get();
			double stock = product.getStock() - (d.getTotalPacked());
			product.setStock(stock);
			productRepository.saveAndFlush(product);
			/**
			 * Create stock card
			 */
			ProductStockCard stockCard = new ProductStockCard();
			stockCard.setQtyOut(d.getTotalPacked());
			stockCard.setProduct(product);
			stockCard.setBalance(stock);
			stockCard.setDay(dayRepository.getCurrentBussinessDay());
			stockCard.setReference("To Packing List. Ref #: "+pcl.getNo());
			productStockCardService.save(stockCard);
			
			/**
			 * Grab totals
			 */
			totalPreviousReturns = totalPreviousReturns + d.getPreviousReturns() * d.getSellingPriceVatIncl();
			totalAmountIssued = totalAmountIssued + d.getQtyIssued() * d.getSellingPriceVatIncl();
			totalAmountPacked = totalAmountPacked + d.getTotalPacked() * d.getSellingPriceVatIncl();
			totalSales = totalSales + d.getQtySold() * d.getSellingPriceVatIncl();
			totalReturns = totalReturns + d.getQtyReturned() * d.getSellingPriceVatIncl();
			totalOffered = totalOffered + d.getQtyOffered() * d.getSellingPriceVatIncl();
			totalDamages = totalDamages + d.getQtyDamaged() * d.getSellingPriceVatIncl();				
		}
		if(totalAmountPacked != totalPreviousReturns + totalAmountIssued) {
			throw new InvalidEntryException("Total packed must be equal to sum of previous returns and amount issued");
		}
		if(totalSales != 0) {
			throw new InvalidEntryException("Invalid entry in sales");
		}
		if(totalReturns != 0) {
			throw new InvalidEntryException("Invalid entry in total returns");
		}
		if(totalOffered != 0) {
			throw new InvalidEntryException("Invalid entry in total offered");
		}
		if(totalDamages != 0) {
			throw new InvalidEntryException("Invalid entry in total damages");
		}
		
		PackingListModel model = new PackingListModel();
		model.setId(pcl.getId());
		model.setNo(pcl.getNo());
		model.setCustomer(pcl.getCustomer());
		model.setStatus(pcl.getStatus());
		model.setIssueDate(pcl.getIssueDate());		
		model.setComments(pcl.getComments());
		if(pcl.getCreatedAt() != null && pcl.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(pcl.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.getCreatedBy()));
		}
		if(pcl.getApprovedAt() != null && pcl.getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(pcl.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.getApprovedBy()));
		}	
		if(pcl.getPostedAt() != null && pcl.getPostedBy() != null) {
			model.setPosted(dayRepository.findById(pcl.getPostedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.getPostedBy()));
		}	
		List<PackingListDetail> packingListDetails = pcl.getPackingListDetails();
		List<PackingListDetailModel> modelDetails = new ArrayList<PackingListDetailModel>();
		for(PackingListDetail d : packingListDetails) {
			PackingListDetailModel detail = new PackingListDetailModel();
			detail.setId(d.getId());
			detail.setPreviousReturns(d.getPreviousReturns());
			detail.setProduct(d.getProduct());
			detail.setQtyDamaged(d.getQtyDamaged());
			detail.setQtyIssued(d.getQtyIssued());
			detail.setTotalPacked(d.getTotalPacked());
			detail.setQtyOffered(d.getQtyOffered());
			detail.setQtyReturned(d.getQtyReturned());
			detail.setQtySold(d.getQtySold());
			detail.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
			detail.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
			modelDetails.add(detail);
		}
		model.setPackingListDetails(modelDetails);
		
		model.setTotalPreviousReturns(totalPreviousReturns);
		model.setTotalAmountIssued(totalAmountIssued);
		model.setTotalAmountPacked(totalAmountPacked);
		model.setTotalSales(totalSales);
		model.setTotalOffered(totalOffered);
		model.setTotalReturns(totalReturns);
		model.setTotalDamages(totalDamages);
		
		model.setTotalDiscounts(totalDiscounts);
		model.setTotalExpenditures(totalExpenditures);
		model.setTotalBank(totalBank);
		model.setTotalCash(totalCash);
		model.setTotalDeficit(totalDeficit);
		
		return model;
	}

	@Override
	public PackingListModel post(PackingList packingList, HttpServletRequest request) {	
		PackingList pcl = packingListRepository.saveAndFlush(packingList);
		List<PackingListDetail> details = pcl.getPackingListDetails();
		Sale sale = new Sale();
		sale.setCreatedAt(dayRepository.getCurrentBussinessDay().getId());
		sale.setCreatedBy(userService.getUserId(request));
		sale.setDay(dayRepository.getCurrentBussinessDay());
		sale.setReference("Packing list sales Ref# "+pcl.getNo());
		sale = saleRepository.saveAndFlush(sale);
		
		double totalPreviousReturns = 0;
		double totalAmountIssued = 0;
		double totalAmountPacked = 0;
		double totalSales = 0;
		double totalReturns = 0;
		double totalOffered = 0;
		double totalDamages = 0;
		double totalDiscounts = pcl.getTotalDiscounts();
		double totalExpenditures = pcl.getTotalExpenditures();
		double totalBank = pcl.getTotalBank();
		double totalCash = pcl.getTotalCash();
		double totalDeficit = pcl.getTotalDeficit();
		
		for(PackingListDetail d : details) {
			/**
			 * Grab stock qty and update stock
			 */
			Product product =productRepository.findById(d.getProduct().getId()).get();
			double stock = product.getStock() + (d.getQtyReturned());
			product.setStock(stock);
			productRepository.saveAndFlush(product);
			/**
			 * Create stock card
			 */
			ProductStockCard stockCard = new ProductStockCard();
			stockCard.setQtyIn(d.getQtyReturned());
			stockCard.setProduct(product);
			stockCard.setBalance(stock);
			stockCard.setDay(dayRepository.getCurrentBussinessDay());
			stockCard.setReference("Returns. Ref #: "+pcl.getNo());
			productStockCardService.save(stockCard);
			/**
			 * Register damages
			 */
			if(d.getQtyDamaged() > 0) {
				ProductDamage damage = new ProductDamage();
				damage.setQty(d.getQtyDamaged());
				damage.setCostPriceVatIncl(d.getCostPriceVatIncl());
				damage.setCostPriceVatExcl(d.getCostPriceVatExcl());
				damage.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
				damage.setSellingPriceVatExcl(d.getCostPriceVatExcl());
				damage.setDay(dayRepository.getCurrentBussinessDay());
				damage.setProduct(d.getProduct());
				damage.setReference("Damaged in packing list. Ref# "+pcl.getNo());
				productDamageService.save(damage);
			}
			
			
			/**
			 * Register offers
			 */
			if(d.getQtyOffered() > 0) {
				ProductOffer offer = new ProductOffer();
				offer.setQty(d.getQtyOffered());
				offer.setCostPriceVatIncl(d.getCostPriceVatIncl());
				offer.setCostPriceVatExcl(d.getCostPriceVatExcl());
				offer.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
				offer.setSellingPriceVatExcl(d.getCostPriceVatExcl());
				offer.setDay(dayRepository.getCurrentBussinessDay());
				offer.setProduct(d.getProduct());
				offer.setReference("Offered in packing list. Ref# "+pcl.getNo());
				productOfferService.save(offer);
			}
			
			/**
			 * Post to sales
			 */
			SaleDetail sd = new SaleDetail();
			sd.setProduct(product);
			sd.setSale(sale);
			sd.setQty(d.getQtySold());
			sd.setCostPriceVatIncl(d.getCostPriceVatIncl());
			sd.setCostPriceVatExcl(d.getCostPriceVatExcl());
			sd.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
			sd.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
			sd.setDiscount(0);
			sd.setTax(0);
			saleDetailRepository.saveAndFlush(sd);
			
			totalPreviousReturns = totalPreviousReturns + d.getPreviousReturns() * d.getSellingPriceVatIncl();
			totalAmountIssued = totalAmountIssued + d.getQtyIssued() * d.getSellingPriceVatIncl();
			totalAmountPacked = totalAmountPacked + d.getTotalPacked() * d.getSellingPriceVatIncl();
			totalSales = totalSales + d.getQtySold() * d.getSellingPriceVatIncl();
			totalReturns = totalReturns + d.getQtyReturned() * d.getSellingPriceVatIncl();
			totalOffered = totalOffered + d.getQtyOffered() * d.getSellingPriceVatIncl();
			totalDamages = totalDamages + d.getQtyDamaged() * d.getSellingPriceVatIncl();
		}
		
		/**
		 * Register Deficit
		 */
		if(totalDeficit > 0) {
			Debt debt = new Debt();
			debt.setAmount(totalDeficit);
			debt.setDay(dayRepository.getCurrentBussinessDay());
			debt.setPackingList(pcl);
			debtService.create(debt);
		}
		
		if(totalDiscounts < 0) {
			throw new InvalidEntryException("Could not process, invalid discounts amount");
		}	
		if(totalExpenditures < 0) {
			throw new InvalidEntryException("Could not process, invalid expenses amount");
		}
		if(totalBank < 0) {
			throw new InvalidEntryException("Could not process, invalid bank amount");
		}
		if(totalCash < 0) {
			throw new InvalidEntryException("Could not process, invalid cash amount");
		}
		if(totalDeficit < 0) {
			throw new InvalidEntryException("Could not process, invalid deficit amount");
		}
		
		if(totalSales != totalDiscounts + totalExpenditures + totalBank + totalCash + totalDeficit) {
			throw new InvalidEntryException("Could not process, amounts do not tally ");
		}
		if(totalAmountPacked != totalSales +totalReturns + totalOffered + totalDamages) {
			throw new InvalidEntryException("Could not process, amounts do not tally");
		}
		
		PackingListModel model = new PackingListModel();
		model.setId(pcl.getId());
		model.setNo(pcl.getNo());
		model.setCustomer(pcl.getCustomer());
		model.setStatus(pcl.getStatus());
		model.setIssueDate(pcl.getIssueDate());		
		model.setComments(pcl.getComments());
		model.setTotalPreviousReturns(totalPreviousReturns);
		model.setTotalAmountIssued(totalAmountIssued);
		model.setTotalAmountPacked(totalAmountPacked);
		model.setTotalSales(totalSales);
		model.setTotalOffered(totalOffered);
		model.setTotalReturns(totalReturns);
		model.setTotalDamages(totalDamages);
		if(pcl.getCreatedAt() != null && pcl.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(pcl.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.getCreatedBy()));
		}
		if(pcl.getApprovedAt() != null && pcl.getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(pcl.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.getApprovedBy()));
		}	
		if(pcl.getPostedAt() != null && pcl.getPostedBy() != null) {
			model.setPosted(dayRepository.findById(pcl.getPostedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(pcl.getPostedBy()));
		}	
		List<PackingListDetail> packingListDetails = pcl.getPackingListDetails();
		List<PackingListDetailModel> modelDetails = new ArrayList<PackingListDetailModel>();
		for(PackingListDetail d : packingListDetails) {
			PackingListDetailModel detail = new PackingListDetailModel();
			detail.setId(d.getId());
			detail.setPreviousReturns(d.getPreviousReturns());
			detail.setProduct(d.getProduct());
			detail.setQtyDamaged(d.getQtyDamaged());
			detail.setQtyIssued(d.getQtyIssued());
			detail.setTotalPacked(d.getTotalPacked());
			detail.setQtyOffered(d.getQtyOffered());
			detail.setQtyReturned(d.getQtyReturned());
			detail.setQtySold(d.getQtySold());
			detail.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
			detail.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
			modelDetails.add(detail);
		}
		model.setPackingListDetails(modelDetails);
		
		model.setTotalPreviousReturns(totalPreviousReturns);
		model.setTotalAmountIssued(totalAmountIssued);
		model.setTotalAmountPacked(totalAmountPacked);
		model.setTotalSales(totalSales);
		model.setTotalOffered(totalOffered);
		model.setTotalReturns(totalReturns);
		model.setTotalDamages(totalDamages);
		
		model.setTotalDiscounts(totalDiscounts);
		model.setTotalExpenditures(totalExpenditures);
		model.setTotalBank(totalBank);
		model.setTotalCash(totalCash);
		model.setTotalDeficit(totalDeficit);
		
		return model;
	}
	
	private boolean validate(PackingList packingList) {
		return true;
	}
	
	private boolean allowDelete(PackingList packingList) {
		return true;
	}
	
	private boolean validateDetail(PackingListDetail packingListDetail) {
		return true;
	}
	
	private boolean allowDeleteDetail(PackingListDetail packingListDetail) {
		return true;
	}
	
	private String generatePackingListNo(PackingList packingList) {
		Long number = packingList.getId();		
		String sNumber = number.toString();
		return "SLR-"+Formater.formatNine(sNumber);
	}
}
