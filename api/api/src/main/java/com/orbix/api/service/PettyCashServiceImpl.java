/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.CashPickUp;
import com.orbix.api.domain.PettyCash;
import com.orbix.api.domain.Till;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.CartDetailRepository;
import com.orbix.api.repositories.CartRepository;
import com.orbix.api.repositories.CashPickUpRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.PackingListDetailRepository;
import com.orbix.api.repositories.PackingListRepository;
import com.orbix.api.repositories.PettyCashRepository;
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
public class PettyCashServiceImpl implements PettyCashService {
	private final CartRepository cartRepository;
	private final CartDetailRepository cartDetailRepository;
	private final ReceiptRepository receiptRepository;
	private final ReceiptDetailRepository receiptDetailRepository;
	private final TillRepository tillRepository;
	private final PettyCashRepository pettyCashRepository;
	
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
	public void pettyCash(double amount, String details, Till till, HttpServletRequest request) {
		if(amount > till.getCash()) {
			throw new InvalidEntryException("Could not process. Amount entered exceeds cash available");
		}
		if(amount <= 0) {
			throw new InvalidEntryException("Could not process. Zero amount is not allowed");
		}
		PettyCash pettyCash = new PettyCash();
		if(amount > till.getCash()) {
			if(amount > till.getCash() + till.getFloatBalance()) {
				throw new InvalidOperationException("Could not process. Insufficient funds");
			}else {
				double tillCash = till.getCash();
				till.setCash(0);
				till.setFloatBalance(till.getFloatBalance() - (amount - tillCash));		
			}
		}else {
			till.setCash(till.getCash() - amount);
		}		
		tillRepository.saveAndFlush(till);
		pettyCash.setNo("NA");
		pettyCash.setAmount(amount);
		pettyCash.setDetails(details);
		pettyCash.setTill(till);
		pettyCash.setCreatedBy(userService.getUserId(request));
		pettyCash.setCreatedAt(dayRepository.getCurrentBussinessDay().getId());
		pettyCashRepository.saveAndFlush(pettyCash);
		pettyCash.setNo(generatePettyCashNo(pettyCash));
		pettyCashRepository.save(pettyCash);				
	}
	
	private String generatePettyCashNo(PettyCash pettyCash) {
		Long number = pettyCash.getId();		
		String sNumber = number.toString();
		return "PTC-"+Formater.formatNine(sNumber);
	}

}
