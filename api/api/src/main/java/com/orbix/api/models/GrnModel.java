/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDate;
import java.util.List;

import com.orbix.api.domain.Lpo;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class GrnModel {
	Long id = null;
	String no;
	String invoiceNo;
	double invoiceAmount;
	String orderNo;
	Lpo lpo = null;
	String status = "";
	LocalDate grnDate = null;
	String created = "";
	String approved = "";
	String comments = "";
	List<GrnDetailModel> grnDetails;
}

