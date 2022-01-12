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
import com.orbix.api.domain.Grn;
import com.orbix.api.domain.GrnDetail;
import com.orbix.api.domain.Lpo;
import com.orbix.api.domain.LpoDetail;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductStockCard;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.GrnDetailModel;
import com.orbix.api.models.GrnModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.GrnDetailRepository;
import com.orbix.api.repositories.GrnRepository;
import com.orbix.api.repositories.LpoDetailRepository;
import com.orbix.api.repositories.LpoRepository;
import com.orbix.api.repositories.ProductRepository;
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
public class GrnServiceImpl implements GrnService {
	
	private final GrnRepository grnRepository;
	private final GrnDetailRepository grnDetailRepository;
	private final LpoRepository lpoRepository;
	private final LpoDetailRepository lpoDetailRepository;
	private final UserRepository userRepository;
	private final DayRepository dayRepository;
	private final ProductRepository productRepository;
	
	private final ProductStockCardService stockCardService;

	@Override
	public GrnModel save(Grn grn) {			
		/**
		 * First check if the GRN has order attached, if not, reject
		 * If yes, check order status, if order status is not printed, reject
		 * If PRINTED, search if there is a GRN associated with the order
		 * 
		 * 
		 * 
		 */
		Optional<Lpo> l = lpoRepository.findByNo(grn.getOrderNo());
		if(!l.isPresent()) {
			/**
			 * Checks if the LPO is present
			 * GRN with a valid LPO will be processed
			 */
			throw new InvalidOperationException("Could not process GRN, LPO not found");
		}
		if(!l.get().getStatus().equals("PRINTED")) {
			/**
			 * Checks if the LPO is valid for receiving.
			 * Only PRINTED LPO can be received
			 */
			throw new InvalidOperationException("Could not process LPO, only PRINTED LPO can be received");
		}
		
		Optional<Grn> grnOther = grnRepository.findByLpo(l.get());
		if(grn.getId() == null && grnOther.isPresent()) {
			throw new InvalidOperationException("Could not process GRN, LPO attached to another GRN");
		}else if(grn.getId() != null && grnOther.get().getId() != grn.getId()) {
			throw new InvalidOperationException("Could not process GRN, LPO attached to another GRN");
		}		
		if(!validate(grn)) {
			throw new InvalidEntryException("Could not save, GRN invalid");
		}
		if(l.isPresent()) {
			grn.setOrderNo(l.get().getNo());
		}
		/**
		 * Create a GRN collection wrapper
		 */
		List<GrnDetail> grnDetails = new ArrayList<GrnDetail>();
		
		if(grn.getId() == null) {
			/**
			 * If it is a new GRN
			 * Grab the lpo details and add them to grn details
			 */
			List<LpoDetail> lpoDetails = lpoDetailRepository.findByLpo(l.get());
			for(LpoDetail d : lpoDetails) {
				GrnDetail gd = new GrnDetail();
				gd.setClientPriceVatIncl(d.getCostPriceVatIncl());
				gd.setClientPriceVatExcl(d.getCostPriceVatExcl());
				gd.setProduct(d.getProduct());
				gd.setQtyOrdered(d.getQty());
				grnDetails.add(gd);				
			}
		}
		/**
		 * Save transient property
		 */
		lpoRepository.saveAndFlush(l.get());
		/**
		 * Save GRN
		 */
		grn.setLpo(l.get());		
		Grn g = grnRepository.saveAndFlush(grn);
		for(GrnDetail d : grnDetails) {
			d.setGrn(g);			
		}
		
		//if(grn.getId() == null) {
			//g.setGrnDetails(grnDetails);
		//}		
		if(g.getNo().equals("NA")) {
			g.setNo(generateGrnNo(g));
			g = grnRepository.saveAndFlush(g);
		}
		if(!grnDetails.isEmpty()) {
			for(GrnDetail d : grnDetails) {
				d.setGrn(g);
				grnDetailRepository.saveAndFlush(d);
			}
		}
		
		GrnModel model = new GrnModel();
		model.setId(g.getId());
		model.setNo(g.getNo());
		model.setOrderNo(g.getOrderNo());
		model.setInvoiceNo(g.getInvoiceNo());
		model.setInvoiceAmount(g.getInvoiceAmount());
		model.setStatus(g.getStatus());
		model.setGrnDate(g.getGrnDate());		
		model.setComments(g.getComments());		
		if(g.getCreatedAt() != null && g.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(g.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(g.getCreatedBy()));
		}
		if(g.getApprovedAt() != null && g.getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(g.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(g.getApprovedBy()));
		}
		List<GrnDetailModel> detailModels = new ArrayList<GrnDetailModel>();
		List<GrnDetail> details = grnDetailRepository.findByGrn(g);
		for(GrnDetail d : details) {
			GrnDetailModel m = new GrnDetailModel();
			m.setClientPriceVatIncl(d.getClientPriceVatIncl());
			m.setClientPriceVatExcl(d.getClientPriceVatExcl());
			m.setSupplierPriceVatIncl(d.getSupplierPriceVatIncl());
			m.setSupplierPriceVatExcl(d.getSupplierPriceVatExcl());
			m.setProduct(d.getProduct());
			m.setQtyOrdered(d.getQtyOrdered());
			m.setQtyReceived(d.getQtyReceived());
			detailModels.add(m);
		}
		model.setGrnDetails(detailModels);		
		return model;
	}

	@Override
	public GrnModel get(Long id) {
		GrnModel model = new GrnModel();
		Optional<Grn> g = grnRepository.findById(id);
		if(!g.isPresent()) {
			throw new NotFoundException("GRN not found");
		}
		model.setId(g.get().getId());
		model.setNo(g.get().getNo());
		model.setInvoiceNo(g.get().getInvoiceNo());
		model.setInvoiceAmount(g.get().getInvoiceAmount());
		model.setOrderNo(g.get().getOrderNo());
		model.setStatus(g.get().getStatus());
		model.setGrnDate(g.get().getGrnDate());		
		model.setComments(g.get().getComments());
		
		if(g.get().getCreatedAt() != null && g.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(g.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(g.get().getCreatedBy()));
		}
		if(g.get().getApprovedAt() != null && g.get().getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(g.get().getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(g.get().getApprovedBy()));
		}
		List<GrnDetail> grnDetails = g.get().getGrnDetails();
		List<GrnDetailModel> modelDetails = new ArrayList<GrnDetailModel>();
		for(GrnDetail d : grnDetails) {
			GrnDetailModel modelDetail = new GrnDetailModel();
			modelDetail.setId(d.getId());
			modelDetail.setProduct(d.getProduct());
			modelDetail.setQtyOrdered(d.getQtyOrdered());
			modelDetail.setQtyReceived(d.getQtyReceived());
			modelDetail.setClientPriceVatIncl(d.getClientPriceVatIncl());
			modelDetail.setClientPriceVatExcl(d.getClientPriceVatExcl());
			modelDetail.setSupplierPriceVatIncl(d.getSupplierPriceVatIncl());
			modelDetail.setSupplierPriceVatExcl(d.getSupplierPriceVatExcl());
			modelDetails.add(modelDetail);
		}
		model.setGrnDetails(modelDetails);
		return model;
	}

	@Override
	public GrnModel getByNo(String no) {
		GrnModel model = new GrnModel();
		Optional<Grn> g = grnRepository.findByNo(no);
		if(!g.isPresent()) {
			throw new NotFoundException("GRN not found");
		}
		model.setId(g.get().getId());
		model.setNo(g.get().getNo());
		model.setInvoiceNo(g.get().getInvoiceNo());
		model.setInvoiceAmount(g.get().getInvoiceAmount());
		model.setOrderNo(g.get().getOrderNo());
		model.setStatus(g.get().getStatus());
		model.setGrnDate(g.get().getGrnDate());		
		model.setComments(g.get().getComments());
		
		if(g.get().getCreatedAt() != null && g.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(g.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(g.get().getCreatedBy()));
		}
		if(g.get().getApprovedAt() != null && g.get().getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(g.get().getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(g.get().getApprovedBy()));
		}
		List<GrnDetail> grnDetails = g.get().getGrnDetails();
		List<GrnDetailModel> modelDetails = new ArrayList<GrnDetailModel>();
		for(GrnDetail d : grnDetails) {
			GrnDetailModel modelDetail = new GrnDetailModel();
			modelDetail.setId(d.getId());
			modelDetail.setProduct(d.getProduct());
			modelDetail.setQtyOrdered(d.getQtyOrdered());
			modelDetail.setQtyReceived(d.getQtyReceived());
			modelDetail.setClientPriceVatIncl(d.getClientPriceVatIncl());
			modelDetail.setClientPriceVatExcl(d.getClientPriceVatExcl());
			modelDetail.setSupplierPriceVatIncl(d.getSupplierPriceVatIncl());
			modelDetail.setSupplierPriceVatExcl(d.getSupplierPriceVatExcl());
			modelDetails.add(modelDetail);
		}
		model.setGrnDetails(modelDetails);
		return model;
	}

	@Override
	public boolean delete(Grn grn) {
		if(!allowDelete(grn)) {
			throw new InvalidOperationException("Deleting the selected GRN is not allowed");
		}
		grnRepository.delete(grn);
		return true;
	}

	@Override
	public List<GrnModel> getAllVisible() {
		List<String> statuses = new ArrayList<String>();
		statuses.add("BLANK");
		statuses.add("PENDING");
		statuses.add("APPROVED");
		statuses.add("RECEIVED");
		List<Grn> grns = grnRepository.findAllVissible(statuses);
		List<GrnModel> models = new ArrayList<GrnModel>();
		for(Grn g : grns) {
			GrnModel model = new GrnModel();
			model.setId(g.getId());
			model.setNo(g.getNo());
			model.setGrnDate(g.getGrnDate());
			model.setInvoiceNo(g.getInvoiceNo());
			model.setInvoiceAmount(g.getInvoiceAmount());
			model.setOrderNo(g.getOrderNo());
			model.setStatus(g.getStatus());
			model.setComments(g.getComments());
			if(g.getCreatedAt() != null && g.getCreatedBy() != null) {
				model.setCreated(dayRepository.findById(g.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(g.getCreatedBy()));
			}
			if(g.getApprovedAt() != null && g.getApprovedBy() != null) {
				model.setApproved(dayRepository.findById(g.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(g.getApprovedBy()));
			}
			models.add(model);
		}
		return models;
	}

	@Override
	public GrnDetailModel saveDetail(GrnDetail grnDetail) {
		if(!validateDetail(grnDetail)) {
			throw new InvalidEntryException("Could not save detail, Invalid entry");
		}
		GrnDetailModel model = new GrnDetailModel();
		GrnDetail l = grnDetailRepository.save(grnDetail);
		model.setId(l.getId());
		model.setProduct(l.getProduct());
		model.setQtyOrdered(l.getQtyOrdered());
		model.setQtyReceived(l.getQtyReceived());
		model.setSupplierPriceVatIncl(l.getSupplierPriceVatIncl());
		model.setSupplierPriceVatExcl(l.getSupplierPriceVatExcl());
		//model.setGrn(l.getGrn());
		return model;
	}

	@Override
	public GrnDetailModel getDetail(Long id) {
		GrnDetailModel model = new GrnDetailModel();
		Optional<GrnDetail> l = grnDetailRepository.findById(id);
		if(!l.isPresent()) {
			throw new NotFoundException("GRN detail not found");
		}
		model.setId(l.get().getId());
		model.setProduct(l.get().getProduct());
		model.setQtyOrdered(l.get().getQtyOrdered());
		model.setQtyReceived(l.get().getQtyReceived());
		model.setClientPriceVatIncl(l.get().getClientPriceVatIncl());
		model.setClientPriceVatExcl(l.get().getClientPriceVatExcl());
		model.setSupplierPriceVatIncl(l.get().getSupplierPriceVatIncl());
		model.setSupplierPriceVatExcl(l.get().getSupplierPriceVatExcl());
		model.setGrn(l.get().getGrn());
		return model;
	}

	@Override
	public List<GrnDetailModel> getAllDetails(Grn grn) {
		List<GrnDetail> details = grnDetailRepository.findByGrn(grn);
		List<GrnDetailModel> models = new ArrayList<GrnDetailModel>();
		for(GrnDetail l : details) {
			GrnDetailModel model = new GrnDetailModel();
			model.setId(l.getId());
			model.setProduct(l.getProduct());
			model.setQtyOrdered(l.getQtyOrdered());
			model.setQtyReceived(l.getQtyReceived());
			model.setClientPriceVatIncl(l.getClientPriceVatIncl());
			model.setClientPriceVatExcl(l.getClientPriceVatExcl());
			model.setSupplierPriceVatIncl(l.getSupplierPriceVatIncl());
			model.setSupplierPriceVatExcl(l.getSupplierPriceVatExcl());
			model.setGrn(l.getGrn());
			models.add(model);
		}
		return models;	
	}
	
	private boolean validate(Grn grn) {
		return true;
	}
	
	private boolean allowDelete(Grn grn) {
		return true;
	}
	
	private boolean validateDetail(GrnDetail grnDetail) {
		return true;
	}
	
	private String generateGrnNo(Grn grn) {
		Long number = grn.getId();		
		String sNumber = number.toString();
		return "GRN-"+Formater.formatSix(sNumber);
	}

	@Override
	public GrnModel receive(Grn grn) {
		/**
		 * First check if GRN is PENDING
		 * Validate GRN
		 * Check all details for validity
		 */
		if(!grn.getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not process, only a pending GRN can be received");
		}
		if(!validate(grn)) {
			throw new InvalidOperationException("Could not process, GRN invalid");
		}
		List<GrnDetail> details = grn.getGrnDetails();
		/**
		 * Here, cross check every detail for validity, if invalid, reject all
		 */
		for(GrnDetail d : details) {
			if(d.getSupplierPriceVatIncl() < 0) {
				throw new InvalidEntryException("Could not process, invalid cost price at ["+d.getProduct().getCode()+"] "+d.getProduct().getDescription()+". Cost price must be positive a value.");
			}
			if(d.getQtyReceived() < 0) {
				throw new InvalidEntryException("Could not process, invalid quantity at ["+d.getProduct().getCode()+"] "+d.getProduct().getDescription()+". Quantity must be positive a value.");
			}
			if(d.getQtyReceived() > d.getQtyOrdered()) {
				throw new InvalidOperationException("Could not process, quantity received exceeds quantity ordered at ["+d.getProduct().getCode()+"] "+d.getProduct().getDescription());
			}
		}
		/**
		 * Passed all validation checks, 
		 * now receive
		 */
		Optional<Lpo> l = lpoRepository.findById(grn.getLpo().getId());
		if(l.isPresent()) {
			l.get().setStatus("RECEIVED");
			lpoRepository.saveAndFlush(l.get());
		}		
		grn.setStatus("RECEIVED");
		grnRepository.saveAndFlush(grn);
		for(GrnDetail d : details) {
			/**
			 * Update stocks
			 * Create stock card
			 * First, take initial stock value
			 * Update stock
			 * Add qty to initial stock value to obtain final stock value
			 * Create stock card with the final stock value
			 */
			/**
			 * Here, update stock card
			 */
			Product product =productRepository.findById(d.getProduct().getId()).get();
			double stock = product.getStock() + d.getQtyReceived();
			product.setStock(stock);
			productRepository.saveAndFlush(product);
			/**
			 * Now create stock card
			 */
			ProductStockCard stockCard = new ProductStockCard();
			stockCard.setQtyIn(d.getQtyReceived());
			stockCard.setProduct(product);
			stockCard.setBalance(stock);
			stockCard.setDay(dayRepository.getCurrentBussinessDay());
			stockCard.setReference("Goods received. Ref #: "+grn.getNo());
			stockCardService.save(stockCard);
		}
		
		
		GrnModel model = new GrnModel();
		Optional<Grn> g = grnRepository.findById(grn.getId());
		if(!g.isPresent()) {
			throw new NotFoundException("GRN not found");
		}
		model.setId(g.get().getId());
		model.setNo(g.get().getNo());
		model.setInvoiceNo(g.get().getInvoiceNo());
		model.setInvoiceAmount(g.get().getInvoiceAmount());
		model.setOrderNo(g.get().getOrderNo());
		model.setStatus(g.get().getStatus());
		model.setGrnDate(g.get().getGrnDate());		
		model.setComments(g.get().getComments());
		
		if(g.get().getCreatedAt() != null && g.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(g.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(g.get().getCreatedBy()));
		}
		if(g.get().getApprovedAt() != null && g.get().getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(g.get().getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(g.get().getApprovedBy()));
		}
		List<GrnDetail> grnDetails = g.get().getGrnDetails();
		List<GrnDetailModel> modelDetails = new ArrayList<GrnDetailModel>();
		for(GrnDetail d : grnDetails) {
			GrnDetailModel modelDetail = new GrnDetailModel();
			modelDetail.setId(d.getId());
			modelDetail.setProduct(d.getProduct());
			modelDetail.setQtyOrdered(d.getQtyOrdered());
			modelDetail.setQtyReceived(d.getQtyReceived());
			modelDetail.setClientPriceVatIncl(d.getClientPriceVatIncl());
			modelDetail.setClientPriceVatExcl(d.getClientPriceVatExcl());
			modelDetail.setSupplierPriceVatIncl(d.getSupplierPriceVatIncl());
			modelDetail.setSupplierPriceVatExcl(d.getSupplierPriceVatExcl());
			modelDetails.add(modelDetail);
		}
		model.setGrnDetails(modelDetails);
		return model;
		
	}

	@Override
	public boolean archive(Grn grn) {
		if(!grn.getStatus().equals("RECEIVED")) {
			throw new InvalidOperationException("Could not process, only a RECEIVED GRN can be archived");
		}
		grn.setStatus("ARCHIVED");
		grnRepository.saveAndFlush(grn);
		return true;
	}

	@Override
	public boolean archiveAll() {
		List<Grn> grns = grnRepository.findAllReceived("RECEIVED");
		if(grns.isEmpty()) {
			throw new NotFoundException("No GRN to archive");
		}
		for(Grn g : grns) {
			g.setStatus("ARCHIVED");
			grnRepository.saveAndFlush(g);
		}
		return true;
	}

}
