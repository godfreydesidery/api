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
import com.orbix.api.domain.Employee;
import com.orbix.api.domain.DebtReceipt;
import com.orbix.api.domain.Employee;
import com.orbix.api.domain.DebtReceipt;
import com.orbix.api.domain.DebtReceipt;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.DebtReceiptModel;
import com.orbix.api.models.DebtReceiptModel;
import com.orbix.api.models.DebtReceiptModel;
import com.orbix.api.repositories.EmployeeRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.DebtReceiptRepository;
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
public class DebtReceiptServiceImpl implements DebtReceiptService {
	
	private final DebtReceiptRepository debtReceiptRepository;
	private final UserRepository userRepository;
	private final EmployeeRepository employeeRepository;
	private final DayRepository dayRepository;
	

	@Override
	public DebtReceiptModel save(DebtReceipt debtReceipt) {
		if(!validate(debtReceipt)) {
			throw new InvalidEntryException("Could not save, Debt receipt invalid");
		}
		DebtReceipt rec = debtReceiptRepository.save(debtReceipt);
		if(rec.getNo().equals("NA")) {
			rec.setNo(generateDebtReceiptNo(rec));
			rec = debtReceiptRepository.save(rec);
		}			
		DebtReceiptModel model = new DebtReceiptModel();
		model.setId(rec.getId());
		model.setNo(rec.getNo());
		model.setEmployee(rec.getEmployee());
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
	public DebtReceiptModel get(Long id) {		
		Optional<DebtReceipt> rec = debtReceiptRepository.findById(id);
		if(!rec.isPresent()) {
			throw new NotFoundException("DebtReceipt not found");
		}
		DebtReceiptModel model = new DebtReceiptModel();
		model.setId(rec.get().getId());
		model.setNo(rec.get().getNo());
		model.setEmployee(rec.get().getEmployee());
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
	public DebtReceiptModel getByNo(String no) {
		Optional<DebtReceipt> rec = debtReceiptRepository.findByNo(no);
		if(!rec.isPresent()) {
			throw new NotFoundException("DebtReceipt not found");
		}
		DebtReceiptModel model = new DebtReceiptModel();
		model.setId(rec.get().getId());
		model.setNo(rec.get().getNo());
		model.setEmployee(rec.get().getEmployee());
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
	public boolean delete(DebtReceipt debtReceipt) {
		if(!allowDelete(debtReceipt)) {
			throw new InvalidOperationException("Deleting the selected Receipt is not allowed");
		}
		debtReceiptRepository.delete(debtReceipt);
		return true;
	}

	@Override
	public List<DebtReceiptModel> getAllVisible() {
		List<String> statuses = new ArrayList<String>();
		statuses.add("PENDING");
		statuses.add("APPROVED");
		List<DebtReceipt> receipts = debtReceiptRepository.findAllVissible(statuses);
		List<DebtReceiptModel> models = new ArrayList<DebtReceiptModel>();
		for(DebtReceipt rec : receipts) {
			DebtReceiptModel model = new DebtReceiptModel();
			model.setId(rec.getId());
			model.setNo(rec.getNo());
			model.setEmployee(rec.getEmployee());
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
	public boolean archive(DebtReceipt debtReceipt) {
		if(!debtReceipt.getStatus().equals("APPROVED")) {
			throw new InvalidOperationException("Could not process, only approved receipt can be archived");
		}
		debtReceipt.setStatus("ARCHIVED");
		debtReceiptRepository.saveAndFlush(debtReceipt);
		return true;
	}

	@Override
	public boolean archiveAll() {
		List<DebtReceipt> receipts = debtReceiptRepository.findAllApproved("APPROVED");
		if(receipts.isEmpty()) {
			throw new NotFoundException("No Receipt to archive");
		}
		for(DebtReceipt r : receipts) {
			if(r.getStatus().equals("APPROVED")) {
				r.setStatus("ARCHIVED");
				debtReceiptRepository.saveAndFlush(r);
			}			
		}
		return true;
	}
	
	private boolean validate(DebtReceipt debtReceipt) {
		if(debtReceipt.getAmount() <= 0) {
			throw new InvalidEntryException("Could not process, amount must be more than zero");
		}
		return true;
	}
	
	private boolean allowDelete(DebtReceipt debtReceipt) {
		return true;
	}
	
	private String generateDebtReceiptNo(DebtReceipt debtReceipt) {
		Long number = debtReceipt.getId();		
		String sNumber = number.toString();
		return "DRC-"+Formater.formatSix(sNumber);
	}

	@Override
	public DebtReceiptModel approve(DebtReceipt debtReceipt) {	
		/**
		 * Approve receipt
		 * Add receipt amount to employee balance
		 */
		DebtReceipt rec = debtReceiptRepository.saveAndFlush(debtReceipt);
		Optional<Employee> c = employeeRepository.findById(rec.getEmployee().getId());
		if(!c.isPresent()) {
			throw new NotFoundException("Employee not found in database");
		}		
		c.get().setBalance(c.get().getBalance() + rec.getAmount());
		employeeRepository.saveAndFlush(c.get());	
		
		DebtReceiptModel model = new DebtReceiptModel();
		model.setId(rec.getId());
		model.setNo(rec.getNo());
		model.setEmployee(rec.getEmployee());
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
