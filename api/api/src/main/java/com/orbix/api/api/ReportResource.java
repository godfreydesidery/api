/**
 * 
 */
package com.orbix.api.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orbix.api.domain.Supplier;
import com.orbix.api.reports.models.DailySalesReport;
import com.orbix.api.reports.models.ProductionReport;
import com.orbix.api.reports.models.SupplySalesReport;
import com.orbix.api.reports.service.ProductionReportService;
import com.orbix.api.reports.service.SalesReportService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportResource {
	
	private final SalesReportService salesReportService;
	private final ProductionReportService productionReportService;
	
	@PostMapping("/reports/daily_sales_report")
	//@PreAuthorize("hasAnyAuthority('CUSTOMER-READ')")
	public ResponseEntity<List<DailySalesReport>> dailySalesReport(
			@RequestBody DailySalesReportArgs args){
		return ResponseEntity.ok().body(salesReportService.getDailySalesReport(args.from, args.to, null, null, null, null, null, null, null, null, null));
	}	
	
	@PostMapping("/reports/supply_sales_report")
	//@PreAuthorize("hasAnyAuthority('CUSTOMER-READ')")
	public ResponseEntity<List<SupplySalesReport>> supplySalesReport(
			@RequestBody SupplySalesReportArgs args){
		return ResponseEntity.ok().body(salesReportService.getSupplySalesReport(args.from, args.to, args.supplier));
	}	
	
	@PostMapping("/reports/production_report")
	//@PreAuthorize("hasAnyAuthority('CUSTOMER-READ')")
	public ResponseEntity<List<ProductionReport>> productionReport(
			@RequestBody ProductionReportArgs args){
		return ResponseEntity.ok().body(productionReportService.getProductionReport(args.from, args.to));
	}	
}

@Data
class DailySalesReportArgs {
	LocalDate from;
	LocalDate to;
	
}

@Data
class SupplySalesReportArgs {
	LocalDate from;
	LocalDate to;
	Supplier supplier;
}

@Data
class ProductionReportArgs {
	LocalDate from;
	LocalDate to;
}
