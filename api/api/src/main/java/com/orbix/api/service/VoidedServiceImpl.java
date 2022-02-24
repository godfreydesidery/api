/**
 * 
 */
package com.orbix.api.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orbix.api.domain.Product;
import com.orbix.api.domain.Till;
import com.orbix.api.domain.Voided;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.PrivilegeRepository;
import com.orbix.api.repositories.RoleRepository;
import com.orbix.api.repositories.ShortcutRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.repositories.VoidedRepository;

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
public class VoidedServiceImpl implements VoidedService {
	
	private final VoidedRepository voidedRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	
	@Override
	public void createVoid(Till till,String barcode, String code, String description, double qty, double sellingPriceVatIncl, HttpServletRequest request, Long createdAt, Long refId) {
		// setCreatedBy(userService.getUserId(request));
		Voided voided = new Voided();
		voided.setTill(till);
		voided.setNo(till.getNo()+refId.toString());
		voided.setBarcode(barcode);
		voided.setCode(code);
		voided.setDescription(description);
		voided.setQty(qty);
		voided.setSellingPriceVatIncl(sellingPriceVatIncl);
		voided.setCreatedBy(userService.getUserId(request));
		voided.setCreatedAt(dayRepository.getCurrentBussinessDay().getId());
		voided.setChecked(false);
		voided.setRefId(refId);
		voidedRepository.saveAndFlush(voided);
	}

	@Override
	public void deleteVoid(Long refId) {
		Optional<Voided> d = voidedRepository.findByRefId(refId);
		if(d.isPresent()) {
			voidedRepository.delete(d.get());
		}
	}

	@Override
	public void checkVoid(Long refId) {
		Optional<Voided> v = voidedRepository.findByRefId(refId);
		if(v.isPresent()) {
			v.get().setChecked(true);
			voidedRepository.saveAndFlush(v.get());
		}
	}
}
