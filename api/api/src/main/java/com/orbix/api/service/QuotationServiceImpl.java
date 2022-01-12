/**
 * 
 */
package com.orbix.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.Quotation;
import com.orbix.api.domain.QuotationDetail;
import com.orbix.api.domain.ProductStockCard;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.QuotationDetailModel;
import com.orbix.api.models.QuotationModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.QuotationDetailRepository;
import com.orbix.api.repositories.QuotationRepository;
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
public class QuotationServiceImpl implements QuotationService {
	private final QuotationRepository quotationRepository;
	private final QuotationDetailRepository quotationDetailRepository;
	private final UserRepository userRepository;
	private final DayRepository dayRepository;
	private final ProductRepository productRepository;
	private final ProductStockCardService stockCardService;
	
	@Override
	public QuotationModel save(Quotation quotation) {
		if(!validate(quotation)) {
			throw new InvalidEntryException("Could not save, Sales Invoice invalid");
		}
		Quotation inv = quotationRepository.save(quotation);
		if(inv.getNo().equals("NA")) {
			inv.setNo(generateQuotationNo(inv));
			inv = quotationRepository.save(inv);
		}			
		QuotationModel model = new QuotationModel();
		model.setId(inv.getId());
		model.setNo(inv.getNo());
		model.setCustomer(inv.getCustomer());
		model.setStatus(inv.getStatus());
		model.setQuotationDate(inv.getQuotationDate());		
		model.setComments(inv.getComments());
		if(inv.getCreatedAt() != null && inv.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(inv.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(inv.getCreatedBy()));
		}
		if(inv.getApprovedAt() != null && inv.getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(inv.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(inv.getApprovedBy()));
		}		
		return model;
	}
	
	@Override
	public QuotationModel get(Long id) {
		QuotationModel model = new QuotationModel();
		Optional<Quotation> inv = quotationRepository.findById(id);
		if(!inv.isPresent()) {
			throw new NotFoundException("Quotation not found");
		}
		model.setId(inv.get().getId());
		model.setNo(inv.get().getNo());
		model.setCustomer(inv.get().getCustomer());
		model.setStatus(inv.get().getStatus());
		model.setQuotationDate(inv.get().getQuotationDate());		
		model.setComments(inv.get().getComments());
		if(inv.get().getCreatedAt() != null && inv.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(inv.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(inv.get().getCreatedBy()));
		}
		if(inv.get().getApprovedAt() != null && inv.get().getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(inv.get().getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(inv.get().getApprovedBy()));
		}		
		List<QuotationDetail> quotationDetails = inv.get().getQuotationDetails();
		List<QuotationDetailModel> modelDetails = new ArrayList<QuotationDetailModel>();
		for(QuotationDetail d : quotationDetails) {
			QuotationDetailModel modelDetail = new QuotationDetailModel();
			modelDetail.setId(d.getId());
			modelDetail.setProduct(d.getProduct());
			modelDetail.setQty(d.getQty());
			modelDetail.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
			modelDetail.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
			modelDetail.setQuotation(d.getQuotation());
			modelDetails.add(modelDetail);
		}
		model.setQuotationDetails(modelDetails);
		return model;
	}
	
	@Override
	public QuotationModel getByNo(String no) {
		QuotationModel model = new QuotationModel();
		Optional<Quotation> inv = quotationRepository.findByNo(no);
		if(!inv.isPresent()) {
			throw new NotFoundException("Quotation not found");
		}
		model.setId(inv.get().getId());
		model.setNo(inv.get().getNo());
		model.setCustomer(inv.get().getCustomer());
		model.setStatus(inv.get().getStatus());
		model.setQuotationDate(inv.get().getQuotationDate());		
		model.setComments(inv.get().getComments());
		if(inv.get().getCreatedAt() != null && inv.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(inv.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(inv.get().getCreatedBy()));
		}
		if(inv.get().getApprovedAt() != null && inv.get().getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(inv.get().getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(inv.get().getApprovedBy()));
		}		
		List<QuotationDetail> quotationDetails = inv.get().getQuotationDetails();
		List<QuotationDetailModel> modelDetails = new ArrayList<QuotationDetailModel>();
		for(QuotationDetail d : quotationDetails) {
			QuotationDetailModel modelDetail = new QuotationDetailModel();
			modelDetail.setId(d.getId());
			modelDetail.setProduct(d.getProduct());
			modelDetail.setQty(d.getQty());
			modelDetail.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
			modelDetail.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
			modelDetail.setQuotation(d.getQuotation());
			modelDetails.add(modelDetail);
		}
		model.setQuotationDetails(modelDetails);
		return model;
	}
	
	@Override
	public boolean delete(Quotation quotation) {
		if(!allowDelete(quotation)) {
			throw new InvalidOperationException("Deleting the selected Sales Invoice is not allowed");
		}
		quotationRepository.delete(quotation);
		return true;
	}
	
	@Override
	public List<QuotationModel> getAllVisible() {
		List<String> statuses = new ArrayList<String>();
		statuses.add("BLANK");
		statuses.add("PENDING");
		statuses.add("APPROVED");
		statuses.add("PARTIAL");
		statuses.add("PAID");
		List<Quotation> quotations = quotationRepository.findAllVissible(statuses);
		List<QuotationModel> models = new ArrayList<QuotationModel>();
		for(Quotation quot : quotations) {
			QuotationModel model = new QuotationModel();
			model.setId(quot.getId());
			model.setNo(quot.getNo());
			model.setCustomer(quot.getCustomer());
			model.setStatus(quot.getStatus());
			model.setQuotationDate(quot.getQuotationDate());		
			model.setComments(quot.getComments());
			if(quot.getCreatedAt() != null && quot.getCreatedBy() != null) {
				model.setCreated(dayRepository.findById(quot.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(quot.getCreatedBy()));
			}
			if(quot.getApprovedAt() != null && quot.getApprovedBy() != null) {
				model.setApproved(dayRepository.findById(quot.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(quot.getApprovedBy()));
			}			
			models.add(model);
		}
		return models;
	}
	
	@Override
	public QuotationDetailModel saveDetail(QuotationDetail quotationDetail) {
		if(!validateDetail(quotationDetail)) {
			throw new InvalidEntryException("Could not save detail, Invalid entry");
		}
		QuotationDetailModel model = new QuotationDetailModel();
		QuotationDetail d = quotationDetailRepository.save(quotationDetail);
		model.setId(d.getId());
		model.setProduct(d.getProduct());
		model.setQty(d.getQty());
		model.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
		model.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
		model.setQuotation(d.getQuotation());
		return model;
	}
	
	@Override
	public QuotationDetailModel getDetail(Long id) {
		QuotationDetailModel model = new QuotationDetailModel();
		Optional<QuotationDetail> d = quotationDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Quotation detail not found");
		}
		model.setId(d.get().getId());
		model.setProduct(d.get().getProduct());
		model.setQty(d.get().getQty());
		model.setSellingPriceVatIncl(d.get().getSellingPriceVatIncl());
		model.setSellingPriceVatExcl(d.get().getSellingPriceVatExcl());
		model.setQuotation(d.get().getQuotation());
		return model;
	}
	
	@Override
	public boolean deleteDetail(QuotationDetail quotationDetail) {
		if(!allowDeleteDetail(quotationDetail)) {
			throw new InvalidOperationException("Deleting the selected Invoice detail is not allowed");
		}
		quotationDetailRepository.delete(quotationDetail);
		return true;
	}
	
	@Override
	public List<QuotationDetailModel> getAllDetails(Quotation quotation) {
		List<QuotationDetail> details = quotationDetailRepository.findByQuotation(quotation);
		List<QuotationDetailModel> models = new ArrayList<QuotationDetailModel>();
		for(QuotationDetail d : details) {
			QuotationDetailModel model = new QuotationDetailModel();
			model.setId(d.getId());
			model.setProduct(d.getProduct());
			model.setQty(d.getQty());
			model.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
			model.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
			model.setQuotation(d.getQuotation());
			models.add(model);
		}
		return models;	
	}
	
	@Override
	public boolean archive(Quotation quotation) {
		if(!quotation.getStatus().equals("PAID")) {
			throw new InvalidOperationException("Could not process, only a fully PAID Invoice can be archived");
		}
		quotation.setStatus("ARCHIVED");
		quotationRepository.saveAndFlush(quotation);
		return true;
	}
	
	@Override
	public boolean archiveAll() {
		// TODO Auto-generated method stub
		return false;
	}	
	
	private boolean validate(Quotation quotation) {
		return true;
	}
	
	private boolean allowDelete(Quotation quotation) {
		return true;
	}
	
	private boolean validateDetail(QuotationDetail quotationDetail) {
		return true;
	}
	
	private boolean allowDeleteDetail(QuotationDetail quotationDetail) {
		return true;
	}
	
	private String generateQuotationNo(Quotation quotation) {
		Long number = quotation.getId();		
		String sNumber = number.toString();
		return "QUT-"+Formater.formatSix(sNumber);
	}
}
