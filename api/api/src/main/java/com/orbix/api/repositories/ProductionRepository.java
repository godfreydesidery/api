/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Production;
import com.orbix.api.reports.models.ProductionReport;
import com.orbix.api.reports.models.SupplySalesReport;

/**
 * @author GODFREY
 *
 */
public interface ProductionRepository extends JpaRepository<Production, Long> {

	/**
	 * @param no
	 * @return
	 */
	Optional<Production> findByNo(String no);
	
	@Query(
			value = "SELECT \r\n" +
					"`products`.`code` AS `code`,\r\n" + 
					"`products`.`description` AS `description`,\r\n" + 
					"SUM(`production_products`.`qty`) AS `qty`\r\n" + 
					"FROM\r\n" + 
					"`production_products`\r\n" + 
					"JOIN `days` ON\r\n" + 
					"`days`.`id`=`production_products`.`verified_at`\r\n" + 
					"JOIN `products` ON\r\n" + 
					"`production_products`.`product_id`=`products`.`id`\r\n" + 				
					"WHERE\r\n" + 
					"`days`.`bussiness_date` BETWEEN :from AND :to \r\n" + 
					"GROUP BY `code`",
					nativeQuery = true					
			)
	List<ProductionReport> getProductionReport(LocalDate from, LocalDate to);

}
