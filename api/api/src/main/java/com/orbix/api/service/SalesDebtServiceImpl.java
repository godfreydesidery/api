/**
 * 
 */
package com.orbix.api.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Debt;
import com.orbix.api.domain.SalesDebt;
import com.orbix.api.domain.SalesDebtHistory;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.SalesDebtHistoryRepository;
import com.orbix.api.repositories.SalesDebtRepository;

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
public class SalesDebtServiceImpl implements SalesDebtService {

	private final SalesDebtRepository salesDebtRepository;
	private final SalesDebtHistoryRepository salesDebtHistoryRepository;
	
	@Override
	public SalesDebt create(SalesDebt salesDebt) {
		SalesDebt d = salesDebtRepository.saveAndFlush(salesDebt);
		if(d.getNo().equals("NA")) {
			d.setNo(generateSalesDebtNo(d));
		}
		d = salesDebtRepository.saveAndFlush(d);
		SalesDebtHistory history = new SalesDebtHistory();
		history.setSalesDebt(d);
		history.setDr(salesDebt.getBalance());
		history.setBalance(salesDebt.getBalance());
		history.setReference("New Sales Debt Created");
		salesDebtHistoryRepository.saveAndFlush(history);
		return d;
	}

	@Override
	public SalesDebt pay(SalesDebt salesDebt, double amount) {
		if(amount <= 0) {
			throw new InvalidEntryException("Invalid payment amount");
		}else if(amount > salesDebt.getBalance()) {
			throw new InvalidEntryException("Payment amount exceeds debt balance");
		}
		salesDebt.setBalance(salesDebt.getBalance() - amount);
		SalesDebt d = salesDebtRepository.saveAndFlush(salesDebt);
		SalesDebtHistory history = new SalesDebtHistory();
		history.setSalesDebt(d);
		history.setCr(amount);
		history.setBalance(salesDebt.getBalance());
		history.setReference("Debt Payment");
		salesDebtHistoryRepository.saveAndFlush(history);
		return d;
	}

	@Override
	public boolean transfer(SalesDebt fromSalesDebt, SalesDebt toSalesDebt, double amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private String generateSalesDebtNo(SalesDebt salesDebt) {
		Long number = salesDebt.getId();		
		String sNumber = number.toString();
		return "DBT-"+Formater.formatNine(sNumber);
	}

}
