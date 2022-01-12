/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Debt;

/**
 * @author GODFREY
 *
 */
public interface DebtRepository extends JpaRepository<Debt, Long> {

}
