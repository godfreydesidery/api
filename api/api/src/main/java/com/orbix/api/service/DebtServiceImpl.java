/**
 * 
 */
package com.orbix.api.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Debt;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.DebtRepository;

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
public class DebtServiceImpl implements DebtService {
	
	private final DebtRepository debtRepository;

	@Override
	public Debt create(Debt debt) {
		/**
		 * First register debt payment, to be implemented later
		 * then pay debt
		 */
		return debtRepository.saveAndFlush(debt);
	}

	@Override
	public Debt pay(Debt debt, double amount) {
		/**
		 * First register debt payment, to be implemented later
		 * then pay debt
		 */
		if(amount <= 0) {
			throw new InvalidEntryException("Invalid amount");
		}else if(amount > debt.getAmount()) {
			throw new InvalidEntryException("Payment amount exceeds debt amount");
		}
		debt.setAmount(debt.getAmount() - amount);
		return debtRepository.saveAndFlush(debt);
	}

}
