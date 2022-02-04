/**
 * 
 */
package com.orbix.api.reports.service;

import java.time.LocalDate;
import java.util.List;

import com.orbix.api.reports.models.ProductionReport;

/**
 * @author GODFREY
 *
 */
public interface ProductionReportService {
	List<ProductionReport> getProductionReport(
			LocalDate from,
			LocalDate to);
}
