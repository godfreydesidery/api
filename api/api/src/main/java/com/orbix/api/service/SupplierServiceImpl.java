/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Supplier;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.SupplierRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GODFREY
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SupplierServiceImpl implements SupplierService {
	
	private final SupplierRepository supplierRepository;

	@Override
	public Supplier save(Supplier supplier) {
		validateSupplier(supplier);
		log.info("Saving customer to the database");
		return supplierRepository.save(supplier);
	}

	@Override
	public Supplier get(Long id) {
		return supplierRepository.findById(id).get();
	}

	@Override
	public Supplier getByName(String name) {
		Optional<Supplier> supplier = supplierRepository.findByName(name);
		if(!supplier.isPresent()) {
			throw new NotFoundException("Supplier not found");
		}
		return supplier.get();
	}

	@Override
	public boolean delete(Supplier supplier) {
		if(allowDelete(supplier)) {
			supplierRepository.delete(supplier);
		}else {
			return false;
		}
		return true;
	}

	@Override
	public List<Supplier> getAll() {
		log.info("Fetching all suppliers");
		return supplierRepository.findAll();
	}
	
	private boolean validateSupplier(Supplier supplier) {
		/**
		 * Put validation logic, throw Invalid exception if not valid
		 */
		
		return true;
	}
	
	private boolean allowDelete(Supplier supplier) {
		/**
		 * Put logic to allow till deletion, return false if not allowed, else return true
		 */
		return true;
	}

	@Override
	public List<String> getNames() {
		return supplierRepository.getActiveNames();
	}


}
