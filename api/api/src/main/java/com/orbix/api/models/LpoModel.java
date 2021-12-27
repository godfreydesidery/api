/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDate;
import java.util.List;

import com.orbix.api.domain.Supplier;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class LpoModel {
	Long id = null;
	String no = "";
	Supplier supplier = null;
	int validityDays = 0;
	String status = "";
	LocalDate orderDate = null;
	LocalDate validUntil = null;
	String created = "";
	String approved = "";
	String printed = "";
	String comments = "";
	List<LpoDetailModel> lpoDetails;
}
