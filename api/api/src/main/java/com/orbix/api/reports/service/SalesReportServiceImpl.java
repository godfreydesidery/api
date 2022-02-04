/**
 * 
 */
package com.orbix.api.reports.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Category;
import com.orbix.api.domain.Class;
import com.orbix.api.domain.Department;
import com.orbix.api.domain.LevelFour;
import com.orbix.api.domain.LevelOne;
import com.orbix.api.domain.LevelThree;
import com.orbix.api.domain.LevelTwo;
import com.orbix.api.domain.SubCategory;
import com.orbix.api.domain.SubClass;
import com.orbix.api.domain.Supplier;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.reports.models.DailySalesReport;
import com.orbix.api.reports.models.SupplySalesReport;
import com.orbix.api.repositories.CategoryRepository;
import com.orbix.api.repositories.ClassRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.DepartmentRepository;
import com.orbix.api.repositories.LevelFourRepository;
import com.orbix.api.repositories.LevelOneRepository;
import com.orbix.api.repositories.LevelThreeRepository;
import com.orbix.api.repositories.LevelTwoRepository;
import com.orbix.api.repositories.SaleDetailRepository;
import com.orbix.api.repositories.SaleRepository;
import com.orbix.api.repositories.SubClassRepository;
import com.orbix.api.repositories.SupplierRepository;

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
public class SalesReportServiceImpl implements SalesReportService {
	
	private final SaleRepository saleRepository;
	private final SaleDetailRepository saleDetailRepository;
	private final DayRepository dayRepository;
	private final DepartmentRepository departmentRepository;
	private final ClassRepository classRepository;
	private final SubClassRepository subClassRepository;
	private final CategoryRepository categoryRepository;
	private final LevelOneRepository levelOneRepository;
	private final LevelTwoRepository levelTwoRepository;
	private final LevelThreeRepository levelThreeRepository;
	private final LevelFourRepository levelFourRepository;
	private final SupplierRepository supplierRepository;
	
	
	@Override
	public List<DailySalesReport> getDailySalesReport(
			LocalDate fromDate, 
			LocalDate toDate, 
			Department department, 
			Class clas,
			SubClass subClass, 
			Category category, 
			SubCategory subCategory, 
			List<LevelOne> levelOnes,
			List<LevelTwo> levelTwos, 
			List<LevelThree> levelThrees, 
			List<LevelFour> levelFours) {
		
		List<DailySalesReport> report = new ArrayList<>();
		if(department == null && category == null && levelOnes == null && levelTwos == null && levelThrees == null && levelFours == null) {
			
		}
		return saleRepository.getDailySalesReport(fromDate, toDate);
	}


	@Override
	public List<SupplySalesReport> getSupplySalesReport(
			LocalDate from,
			LocalDate to,
			Supplier supplier) {
		Optional<Supplier> s = supplierRepository.findByName(supplier.getName());
		if(!s.isPresent()) {
			throw new NotFoundException("Supplier not found");
		}
		return saleRepository.getSupplySalesReport(from, to, supplier.getName());
	}

}
