/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Grn;
import com.orbix.api.domain.GrnDetail;

/**
 * @author GODFREY
 *
 */
public interface GrnDetailRepository extends JpaRepository<GrnDetail, Long> {

	/**
	 * @param g
	 * @return
	 */
	List<GrnDetail> findByGrn(Grn g);
	
}
