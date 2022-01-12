/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Lpo;

/**
 * @author GODFREY
 *
 */
public interface LpoRepository extends JpaRepository<Lpo, Long> {

	/**
	 * @param no
	 * @return
	 */
	Optional<Lpo> findByNo(String no);
	
	@Query("SELECT MAX(l.id) FROM Lpo l")
	Long getLastId();
	
	@Query("SELECT l FROM Lpo l WHERE l.status IN (:statuses)")
	List<Lpo> findAllVissible(List<String> statuses);
	
	@Query("SELECT l FROM Lpo l WHERE l.status =:status")
	List<Lpo> findAllReceived(String status);
}
