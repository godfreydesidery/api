/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Allocation;

/**
 * @author GODFREY
 *
 */
public interface AllocationRepository extends JpaRepository<Allocation, Long> {

}
