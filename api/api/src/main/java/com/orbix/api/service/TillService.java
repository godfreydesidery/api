/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Till;

/**
 * @author GODFREY
 *
 */
public interface TillService {
	Till saveTill(Till till);
	Till getTill(Long id);
	Till getTillByTillNo(String tillNo);
	boolean deleteTill(Till till);
	List<Till>getTills(); //edit this to limit the number, for perfomance.
}
