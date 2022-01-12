/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Grn;
import com.orbix.api.domain.Lpo;

/**
 * @author GODFREY
 *
 */
public interface GrnRepository extends JpaRepository<Grn, Long> {

	/**
	 * @param lpo
	 * @return
	 */
	Optional<Grn> findByLpo(Lpo lpo);

	/**
	 * @param no
	 * @return
	 */
	Optional<Grn> findByNo(String no);
	
	@Query("SELECT g FROM Grn g WHERE g.status IN (:statuses)")
	List<Grn> findAllVissible(List<String> statuses);
	
	@Query("SELECT g FROM Grn g WHERE g.status =:status")
	List<Grn> findAllReceived(String status);

}
