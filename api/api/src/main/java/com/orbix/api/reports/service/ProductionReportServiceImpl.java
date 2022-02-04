/**
 * 
 */
package com.orbix.api.reports.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Supplier;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.reports.models.ProductionReport;
import com.orbix.api.reports.models.SupplySalesReport;
import com.orbix.api.repositories.ProductionRepository;

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
public class ProductionReportServiceImpl implements ProductionReportService {
	
	private final ProductionRepository productionRepository;
	
	@Override
	public List<ProductionReport> getProductionReport(
			LocalDate from,
			LocalDate to) {		
		return productionRepository.getProductionReport(from, to);
	}
}
