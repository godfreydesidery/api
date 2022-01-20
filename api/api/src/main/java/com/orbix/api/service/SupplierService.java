/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Supplier;

/**
 * @author GODFREY
 *
 */
public interface SupplierService {
	Supplier save(Supplier supplier);
	Supplier get(Long id);
	Supplier getByCode(String code);
	Supplier getByName(String name);
	boolean delete(Supplier supplier);
	List<Supplier>getAll(); //edit this to limit the number, for perfomance.
	List<String> getNames();
}
