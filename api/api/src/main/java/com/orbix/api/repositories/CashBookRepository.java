/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.CashBook;

/**
 * @author GODFREY
 *
 */
public interface CashBookRepository extends JpaRepository<CashBook, Long> {

}
