/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Department;

/**
 * @author GODFREY
 *
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<Department> findByName(String name);

}
