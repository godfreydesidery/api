/**
 * 
 */
package com.orbix.api.service;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Material;
import com.orbix.api.domain.MaterialStockCard;
import com.orbix.api.repositories.MaterialStockCardRepository;

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
public class MaterialStockCardServiceImpl implements MaterialStockCardService {
	
	private final MaterialStockCardRepository materialStockCardRepository;

	@Override
	public MaterialStockCard save(MaterialStockCard materialStockCard) {
		return materialStockCardRepository.saveAndFlush(materialStockCard);
	}

	@Override
	public List<MaterialStockCard> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MaterialStockCard> getByMaterial(Material material) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MaterialStockCard> getByDate(LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
