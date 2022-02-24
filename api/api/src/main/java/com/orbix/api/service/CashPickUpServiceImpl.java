/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.CashPickUp;
import com.orbix.api.domain.PackingList;
import com.orbix.api.domain.Till;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.repositories.CartDetailRepository;
import com.orbix.api.repositories.CartRepository;
import com.orbix.api.repositories.CashPickUpRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.PackingListDetailRepository;
import com.orbix.api.repositories.PackingListRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.ReceiptDetailRepository;
import com.orbix.api.repositories.ReceiptRepository;
import com.orbix.api.repositories.SaleDetailRepository;
import com.orbix.api.repositories.SaleRepository;
import com.orbix.api.repositories.TillRepository;
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
public class CashPickUpServiceImpl implements CashPickUpService {
	
	private final CartRepository cartRepository;
	private final CartDetailRepository cartDetailRepository;
	private final ReceiptRepository receiptRepository;
	private final ReceiptDetailRepository receiptDetailRepository;
	private final TillRepository tillRepository;
	private final CashPickUpRepository cashPickUpRepository;
	
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
	public void pickUp(double amount, Till till, HttpServletRequest request) {
		if(amount > till.getCash()) {
			throw new InvalidEntryException("Could not process. Amount entered exceeds cash available");
		}
		if(amount <= 0) {
			throw new InvalidEntryException("Could not process. Zero amount is not allowed");
		}
		CashPickUp cashPickUp = new CashPickUp();
		till.setCash(till.getCash() - amount);
		tillRepository.saveAndFlush(till);
		cashPickUp.setNo("NA");
		cashPickUp.setAmount(amount);
		cashPickUp.setTill(till);
		cashPickUp.setCreatedBy(userService.getUserId(request));
		cashPickUp.setCreatedAt(dayRepository.getCurrentBussinessDay().getId());
		cashPickUpRepository.saveAndFlush(cashPickUp);
		cashPickUp.setNo(generateCashPickUpNo(cashPickUp));
		cashPickUp = cashPickUpRepository.save(cashPickUp);				
	}
	
	private String generateCashPickUpNo(CashPickUp cashPickUp) {
		Long number = cashPickUp.getId();		
		String sNumber = number.toString();
		return "CPU-"+Formater.formatNine(sNumber);
	}

}
