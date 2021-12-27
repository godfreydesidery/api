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
import com.orbix.api.domain.Lpo;
import com.orbix.api.domain.LpoDetail;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.LpoDetailModel;
import com.orbix.api.models.LpoModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.LpoDetailRepository;
import com.orbix.api.repositories.LpoRepository;
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
public class LpoServiceImpl implements LpoService {
	
	private final LpoRepository lpoRepository;
	private final LpoDetailRepository lpoDetailRepository;
	private final UserRepository userRepository;
	private final DayRepository dayRepository;
	
	@Override
	public LpoModel save(Lpo lpo) {
		if(!validate(lpo)) {
			throw new InvalidEntryException("Could not save, LPO invalid");
		}
		Lpo l = lpoRepository.save(lpo);
		if(l.getNo().equals("NA")) {
			l.setNo(generateLpoNo(l));
			l = lpoRepository.save(l);
		}			
		LpoModel model = new LpoModel();
		model.setId(l.getId());
		model.setNo(l.getNo());
		model.setSupplier(l.getSupplier());
		model.setStatus(l.getStatus());
		model.setOrderDate(l.getOrderDate());		
		model.setValidityDays(l.getValidityDays());
		model.setValidUntil(l.getValidUntil());
		model.setComments(l.getComments());
		if(l.getCreateAt() != null && l.getCreateBy() != null) {
			model.setCreated(dayRepository.findById(l.getCreateAt()).get().getBussinessDate() +" "+ userRepository.getAlias(l.getCreateBy()));
		}
		if(l.getApprovedAt() != null && l.getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(l.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(l.getApprovedBy()));
		}
		if(l.getPrintedAt() != null && l.getPrintedBy() != null) {
			model.setPrinted(dayRepository.findById(l.getPrintedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(l.getPrintedBy()));
		}
		return model;
	}
	@Override
	public LpoModel get(Long id) {
		LpoModel model = new LpoModel();
		Optional<Lpo> l = lpoRepository.findById(id);
		if(!l.isPresent()) {
			throw new NotFoundException("LPO not found");
		}
		model.setId(l.get().getId());
		model.setNo(l.get().getNo());
		model.setSupplier(l.get().getSupplier());
		model.setStatus(l.get().getStatus());
		model.setOrderDate(l.get().getOrderDate());		
		model.setValidityDays(l.get().getValidityDays());
		model.setValidUntil(l.get().getValidUntil());
		model.setComments(l.get().getComments());
		if(l.get().getCreateAt() != null && l.get().getCreateBy() != null) {
			model.setCreated(dayRepository.findById(l.get().getCreateAt()).get().getBussinessDate() +" "+ userRepository.getAlias(l.get().getCreateBy()));
		}
		if(l.get().getApprovedAt() != null && l.get().getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(l.get().getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(l.get().getApprovedBy()));
		}
		if(l.get().getPrintedAt() != null && l.get().getPrintedBy() != null) {
			model.setPrinted(dayRepository.findById(l.get().getPrintedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(l.get().getPrintedBy()));
		}
		List<LpoDetail> lpoDetails = l.get().getLpoDetails();
		List<LpoDetailModel> modelDetails = new ArrayList<LpoDetailModel>();
		for(LpoDetail d : lpoDetails) {
			LpoDetailModel modelDetail = new LpoDetailModel();
			modelDetail.setId(d.getId());
			modelDetail.setProduct(d.getProduct());
			modelDetail.setQty(d.getQty());
			modelDetail.setCostPriceVatIncl(d.getCostPriceVatIncl());
			modelDetail.setCostPriceVatExcl(d.getCostPriceVatExcl());
			modelDetail.setLpo(d.getLpo());
			modelDetails.add(modelDetail);
		}
		model.setLpoDetails(modelDetails);
		return model;
	}
	@Override
	public LpoModel getByNo(String no) {
		LpoModel model = new LpoModel();
		Optional<Lpo> l = lpoRepository.findByNo(no);
		if(!l.isPresent()) {
			throw new NotFoundException("LPO not found");
		}
		model.setId(l.get().getId());
		model.setNo(l.get().getNo());
		model.setSupplier(l.get().getSupplier());
		model.setStatus(l.get().getStatus());
		model.setOrderDate(l.get().getOrderDate());		
		model.setValidityDays(l.get().getValidityDays());
		model.setValidUntil(l.get().getValidUntil());
		model.setComments(l.get().getComments());
		if(l.get().getCreateAt() != null && l.get().getCreateBy() != null) {
			model.setCreated(dayRepository.findById(l.get().getCreateAt()).get().getBussinessDate() +" "+ userRepository.getAlias(l.get().getCreateBy()));
		}
		if(l.get().getApprovedAt() != null && l.get().getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(l.get().getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(l.get().getApprovedBy()));
		}
		if(l.get().getPrintedAt() != null && l.get().getPrintedBy() != null) {
			model.setPrinted(dayRepository.findById(l.get().getPrintedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(l.get().getPrintedBy()));
		}
		List<LpoDetail> lpoDetails = l.get().getLpoDetails();
		List<LpoDetailModel> modelDetails = new ArrayList<LpoDetailModel>();
		for(LpoDetail d : lpoDetails) {
			LpoDetailModel modelDetail = new LpoDetailModel();
			modelDetail.setId(d.getId());
			modelDetail.setProduct(d.getProduct());
			modelDetail.setQty(d.getQty());
			modelDetail.setCostPriceVatIncl(d.getCostPriceVatIncl());
			modelDetail.setCostPriceVatExcl(d.getCostPriceVatExcl());
			modelDetail.setLpo(d.getLpo());
			modelDetails.add(modelDetail);
		}
		model.setLpoDetails(modelDetails);
		return model;
	}
	@Override
	public boolean delete(Lpo lpo) {
		if(!allowDelete(lpo)) {
			throw new InvalidOperationException("Deleting the selected LPO is not allowed");
		}
		lpoRepository.delete(lpo);
		return true;
	}
	
	@Override
	public List<LpoModel> getAllVisible() {
		List<String> statuses = new ArrayList<String>();
		statuses.add("BLANK");
		statuses.add("PENDING");
		statuses.add("APPROVED");
		statuses.add("PRINTED");
		List<Lpo> lpos = lpoRepository.findAllVissible(statuses);
		List<LpoModel> models = new ArrayList<LpoModel>();
		for(Lpo l : lpos) {
			LpoModel model = new LpoModel();
			model.setId(l.getId());
			model.setNo(l.getNo());
			model.setSupplier(l.getSupplier());
			model.setStatus(l.getStatus());
			model.setOrderDate(l.getOrderDate());		
			model.setValidityDays(l.getValidityDays());
			model.setValidUntil(l.getValidUntil());
			model.setComments(l.getComments());
			if(l.getCreateAt() != null && l.getCreateBy() != null) {
				model.setCreated(dayRepository.findById(l.getCreateAt()).get().getBussinessDate() +" "+ userRepository.getAlias(l.getCreateBy()));
			}
			if(l.getApprovedAt() != null && l.getApprovedBy() != null) {
				model.setApproved(dayRepository.findById(l.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(l.getApprovedBy()));
			}
			if(l.getPrintedAt() != null && l.getPrintedBy() != null) {
				model.setPrinted(dayRepository.findById(l.getPrintedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(l.getPrintedBy()));
			}
			models.add(model);
		}
		return models;		
	}
	@Override
	public LpoDetailModel saveDetail(LpoDetail lpoDetail) {
		if(!validateDetail(lpoDetail)) {
			throw new InvalidEntryException("Could not save detail, Invalid entry");
		}
		LpoDetailModel model = new LpoDetailModel();
		LpoDetail l = lpoDetailRepository.save(lpoDetail);
		model.setId(l.getId());
		model.setProduct(l.getProduct());
		model.setQty(l.getQty());
		model.setCostPriceVatIncl(l.getCostPriceVatIncl());
		model.setCostPriceVatExcl(l.getCostPriceVatExcl());
		model.setLpo(l.getLpo());
		return model;
	}
	@Override
	public LpoDetailModel getDetail(Long id) {
		LpoDetailModel model = new LpoDetailModel();
		Optional<LpoDetail> l = lpoDetailRepository.findById(id);
		if(!l.isPresent()) {
			throw new NotFoundException("LPO detail not found");
		}
		model.setId(l.get().getId());
		model.setProduct(l.get().getProduct());
		model.setQty(l.get().getQty());
		model.setCostPriceVatIncl(l.get().getCostPriceVatIncl());
		model.setCostPriceVatExcl(l.get().getCostPriceVatExcl());
		model.setLpo(l.get().getLpo());
		return model;
	}
	@Override
	public boolean deleteDetail(LpoDetail lpoDetail) {
		if(!allowDeleteDetail(lpoDetail)) {
			throw new InvalidOperationException("Deleting the selected LPO detail is not allowed");
		}
		lpoDetailRepository.delete(lpoDetail);
		return true;
	}
	@Override
	public List<LpoDetailModel> getAllDetails(Lpo lpo) {
		List<LpoDetail> details = lpoDetailRepository.findByLpo(lpo);
		List<LpoDetailModel> models = new ArrayList<LpoDetailModel>();
		for(LpoDetail l : details) {
			LpoDetailModel model = new LpoDetailModel();
			model.setId(l.getId());
			model.setProduct(l.getProduct());
			model.setQty(l.getQty());
			model.setCostPriceVatIncl(l.getCostPriceVatIncl());
			model.setCostPriceVatExcl(l.getCostPriceVatExcl());
			model.setLpo(l.getLpo());
			models.add(model);
		}
		return models;	
	}
	
	private boolean validate(Lpo lpo) {
		return true;
	}
	
	private boolean allowDelete(Lpo lpo) {
		return true;
	}
	
	private boolean validateDetail(LpoDetail lpoDetail) {
		return true;
	}
	
	private boolean allowDeleteDetail(LpoDetail lpoDetail) {
		return true;
	}
	
	private String generateLpoNo(Lpo lpo) {
		Long number = lpo.getId();		
		String sNumber = number.toString();
		return "LPO-"+Formater.formatSix(sNumber);
	}
	
		
}

