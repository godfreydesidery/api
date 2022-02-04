/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Debt;
import com.orbix.api.domain.Employee;

/**
 * @author GODFREY
 *
 */
public interface DebtRepository extends JpaRepository<Debt, Long> {
	
	/**
	 * @param customer
	 * @param string
	 * @return
	 */
	@Query("SELECT e FROM Debt e WHERE e.employee =:employee AND e.status IN (:statuses)")
	List<Debt> findByEmployeeAndPendingOrPartial(Employee employee, List<String> statuses);
}
