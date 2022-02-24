/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Floatt;
import com.orbix.api.domain.Till;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.FloatRepository;
import com.orbix.api.repositories.TillRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FloatServiceImpl implements FloatService {
	private final TillRepository tillRepository;
	private final FloatRepository floatRepository;
	
	private final UserService userService;
	private final DayRepository dayRepository;
	@Override
	public void addFloat(double amount, Till till, HttpServletRequest request) {		
		if(amount <= 0) {
			throw new InvalidEntryException("Could not process. Zero amount is not allowed");
		}
		Floatt float_ = new Floatt();
		double originalBalance = till.getFloatBalance();
		till.setFloatBalance(till.getFloatBalance() + amount);
		tillRepository.saveAndFlush(till);
		float_.setNo("NA");
		float_.setAddition(amount);
		float_.setBalance(originalBalance + amount); 
		float_.setTill(till);
		float_.setCreatedBy(userService.getUserId(request));
		float_.setCreatedAt(dayRepository.getCurrentBussinessDay().getId());
		float_ = floatRepository.saveAndFlush(float_);
		float_.setNo(generateFloatNo(float_));
		floatRepository.saveAndFlush(float_);				
	}
	
	@Override
	public void deductFloat(double amount, Till till, HttpServletRequest request) {
		if(amount > till.getFloatBalance()) {
			throw new InvalidEntryException("Could not process. Amount entered exceeds float available");
		}
		if(amount <= 0) {
			throw new InvalidEntryException("Could not process. Zero amount is not allowed");
		}
		Floatt float_ = new Floatt();
		double originalBalance = till.getFloatBalance();
		till.setFloatBalance(till.getFloatBalance() - amount);
		tillRepository.saveAndFlush(till);
		float_.setNo("NA");
		float_.setDeduction(amount);
		float_.setBalance(originalBalance - amount); 
		float_.setTill(till);
		float_.setCreatedBy(userService.getUserId(request));
		float_.setCreatedAt(dayRepository.getCurrentBussinessDay().getId());
		float_ = floatRepository.saveAndFlush(float_);
		float_.setNo(generateFloatNo(float_));
		floatRepository.saveAndFlush(float_);		
	}
	
	private String generateFloatNo(Floatt float_) {
		Long number = float_.getId();		
		String sNumber = number.toString();
		return "FLT-"+Formater.formatNine(sNumber);
	}
}
