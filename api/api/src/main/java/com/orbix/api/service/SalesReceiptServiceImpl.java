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
import com.orbix.api.domain.Customer;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductStockCard;
import com.orbix.api.domain.SalesInvoice;
import com.orbix.api.domain.SalesInvoiceDetail;
import com.orbix.api.domain.SalesReceipt;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.SalesInvoiceModel;
import com.orbix.api.models.SalesReceiptModel;
import com.orbix.api.repositories.CustomerRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.SalesReceiptRepository;
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
public class SalesReceiptServiceImpl implements SalesReceiptService {
	
	private final SalesReceiptRepository salesReceiptRepository;
	private final UserRepository userRepository;
	private final CustomerRepository customerRepository;
	private final DayRepository dayRepository;
	

	@Override
	public SalesReceiptModel save(SalesReceipt salesReceipt) {
		if(!validate(salesReceipt)) {
			throw new InvalidEntryException("Could not save, Sales receipt invalid");
		}
		SalesReceipt rec = salesReceiptRepository.save(salesReceipt);
		if(rec.getNo().equals("NA")) {
			rec.setNo(generateSalesReceiptNo(rec));
			rec = salesReceiptRepository.save(rec);
		}			
		SalesReceiptModel model = new SalesReceiptModel();
		model.setId(rec.getId());
		model.setNo(rec.getNo());
		model.setCustomer(rec.getCustomer());
		model.setStatus(rec.getStatus());
		model.setReceiptDate(rec.getReceiptDate());	
		model.setMode(rec.getMode());
		model.setChequeNo(rec.getChequeNo());
		model.setComments(rec.getComments());
		if(rec.getCreatedAt() != null && rec.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(rec.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(rec.getCreatedBy()));
		}
		if(rec.getApprovedAt() != null && rec.getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(rec.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(rec.getApprovedBy()));
		}		
		return model;
	}

	@Override
	public SalesReceiptModel get(Long id) {		
		Optional<SalesReceipt> rec = salesReceiptRepository.findById(id);
		if(!rec.isPresent()) {
			throw new NotFoundException("SalesInvoice not found");
		}
		SalesReceiptModel model = new SalesReceiptModel();
		model.setId(rec.get().getId());
		model.setNo(rec.get().getNo());
		model.setCustomer(rec.get().getCustomer());
		model.setStatus(rec.get().getStatus());
		model.setReceiptDate(rec.get().getReceiptDate());	
		model.setMode(rec.get().getMode());
		model.setChequeNo(rec.get().getChequeNo());
		model.setAmount(rec.get().getAmount());
		model.setComments(rec.get().getComments());
		if(rec.get().getCreatedAt() != null && rec.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(rec.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(rec.get().getCreatedBy()));
		}
		if(rec.get().getApprovedAt() != null && rec.get().getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(rec.get().getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(rec.get().getApprovedBy()));
		}		
		return model;
		
	}

	@Override
	public SalesReceiptModel getByNo(String no) {
		Optional<SalesReceipt> rec = salesReceiptRepository.findByNo(no);
		if(!rec.isPresent()) {
			throw new NotFoundException("SalesInvoice not found");
		}
		SalesReceiptModel model = new SalesReceiptModel();
		model.setId(rec.get().getId());
		model.setNo(rec.get().getNo());
		model.setCustomer(rec.get().getCustomer());
		model.setStatus(rec.get().getStatus());
		model.setReceiptDate(rec.get().getReceiptDate());	
		model.setMode(rec.get().getMode());
		model.setChequeNo(rec.get().getChequeNo());
		model.setAmount(rec.get().getAmount());
		model.setComments(rec.get().getComments());
		if(rec.get().getCreatedAt() != null && rec.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(rec.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(rec.get().getCreatedBy()));
		}
		if(rec.get().getApprovedAt() != null && rec.get().getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(rec.get().getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(rec.get().getApprovedBy()));
		}		
		return model;
	}

	@Override
	public boolean delete(SalesReceipt salesReceipt) {
		if(!allowDelete(salesReceipt)) {
			throw new InvalidOperationException("Deleting the selected Receipt is not allowed");
		}
		salesReceiptRepository.delete(salesReceipt);
		return true;
	}

	@Override
	public List<SalesReceiptModel> getAllVisible() {
		List<String> statuses = new ArrayList<String>();
		statuses.add("PENDING");
		statuses.add("APPROVED");
		List<SalesReceipt> receipts = salesReceiptRepository.findAllVissible(statuses);
		List<SalesReceiptModel> models = new ArrayList<SalesReceiptModel>();
		for(SalesReceipt rec : receipts) {
			SalesReceiptModel model = new SalesReceiptModel();
			model.setId(rec.getId());
			model.setNo(rec.getNo());
			model.setCustomer(rec.getCustomer());
			model.setStatus(rec.getStatus());
			model.setReceiptDate(rec.getReceiptDate());	
			model.setMode(rec.getMode());
			model.setChequeNo(rec.getChequeNo());
			model.setComments(rec.getComments());
			if(rec.getCreatedAt() != null && rec.getCreatedBy() != null) {
				model.setCreated(dayRepository.findById(rec.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(rec.getCreatedBy()));
			}
			if(rec.getApprovedAt() != null && rec.getApprovedBy() != null) {
				model.setApproved(dayRepository.findById(rec.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(rec.getApprovedBy()));
			}		
			models.add(model);
		}
		return models;
	}

	@Override
	public boolean archive(SalesReceipt salesReceipt) {
		if(!salesReceipt.getStatus().equals("APPROVED")) {
			throw new InvalidOperationException("Could not process, only approved receipt can be archived");
		}
		salesReceipt.setStatus("ARCHIVED");
		salesReceiptRepository.saveAndFlush(salesReceipt);
		return true;
	}

	@Override
	public boolean archiveAll() {
		List<SalesReceipt> receipts = salesReceiptRepository.findAllApproved("APPROVED");
		if(receipts.isEmpty()) {
			throw new NotFoundException("No Receipt to archive");
		}
		for(SalesReceipt r : receipts) {
			if(r.getStatus().equals("APPROVED")) {
				r.setStatus("ARCHIVED");
				salesReceiptRepository.saveAndFlush(r);
			}			
		}
		return true;
	}
	
	private boolean validate(SalesReceipt salesReceipt) {
		if(salesReceipt.getAmount() <= 0) {
			throw new InvalidEntryException("Could not process, amount must be more than zero");
		}
		return true;
	}
	
	private boolean allowDelete(SalesReceipt salesReceipt) {
		return true;
	}
	
	private String generateSalesReceiptNo(SalesReceipt salesReceipt) {
		Long number = salesReceipt.getId();		
		String sNumber = number.toString();
		return "SRC-"+Formater.formatSix(sNumber);
	}

	@Override
	public SalesReceiptModel approve(SalesReceipt salesReceipt) {	
		/**
		 * Approve receipt
		 * Add receipt amount to customer balance
		 */
		SalesReceipt rec = salesReceiptRepository.saveAndFlush(salesReceipt);
		Optional<Customer> c = customerRepository.findById(rec.getCustomer().getId());
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found in database");
		}		
		c.get().setBalance(c.get().getBalance() + rec.getAmount());
		customerRepository.saveAndFlush(c.get());	
		
		SalesReceiptModel model = new SalesReceiptModel();
		model.setId(rec.getId());
		model.setNo(rec.getNo());
		model.setCustomer(rec.getCustomer());
		model.setStatus(rec.getStatus());
		model.setReceiptDate(rec.getReceiptDate());	
		model.setMode(rec.getMode());
		model.setChequeNo(rec.getChequeNo());
		model.setComments(rec.getComments());
		if(rec.getCreatedAt() != null && rec.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(rec.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(rec.getCreatedBy()));
		}
		if(rec.getApprovedAt() != null && rec.getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(rec.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(rec.getApprovedBy()));
		}		
		return model;
	}

}
