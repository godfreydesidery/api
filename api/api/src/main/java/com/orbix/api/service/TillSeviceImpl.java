/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Till;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.TillRepository;
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
public class TillSeviceImpl implements TillService {
	
	private final TillRepository tillRepository;
	

	@Override
	public Till saveTill(Till till) {
		validateTill(till);
		log.info("Saving till to the database");
		return tillRepository.save(till);
	}

	@Override
	public Till getTill(Long id) {
		return tillRepository.findById(id).get();
	}

	@Override
	public Till getTillByTillNo(String tillNo) {
		Optional<Till> till = tillRepository.findByTillNo(tillNo);
		if(!till.isPresent()) {
			throw new NotFoundException("Till not found");
		}
		return till.get();
	}

	@Override
	public boolean deleteTill(Till till) {
		if(allowDelete(till)) {
			tillRepository.delete(till);
		}else {
			return false;
		}
		return true;
	}

	@Override
	public List<Till> getTills() {
		log.info("Fetching all tills");
		return tillRepository.findAll();
	}
	
	
	private boolean validateTill(Till till) {
		/**
		 * Put validation logic, throw Invalid exception if not valid
		 */
		
		return true;
	}
	
	private boolean allowDelete(Till till) {
		/**
		 * Put logic to allow till deletion, return false if not allowed, else return true
		 */
		return true;
	}

}
