/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.PackingList;

/**
 * @author GODFREY
 *
 */
public interface PackingListRepository extends JpaRepository<PackingList, Long> {

	/**
	 * @param no
	 * @return
	 */
	Optional<PackingList> findByNo(String no);
	
	@Query("SELECT p FROM PackingList p WHERE p.status IN (:statuses)")
	List<PackingList> findAllVissible(List<String> statuses);
	
	@Query("SELECT p FROM PackingList p WHERE p.status =:status")
	List<PackingList> findAllPosted(String status);

}
