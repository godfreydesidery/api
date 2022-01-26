/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.DebtAllocation;

/**
 * @author GODFREY
 *
 */
public interface DebtAllocationRepository extends JpaRepository<DebtAllocation, Long> {

}
