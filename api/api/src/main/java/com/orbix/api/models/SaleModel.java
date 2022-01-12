/**
 * 
 */
package com.orbix.api.models;

import java.util.List;

import com.orbix.api.domain.Day;
import com.orbix.api.domain.Till;

import lombok.Data;

/**
 * @author GODFREY
 *
 */
@Data
public class SaleModel {
	Long id = null;	
	String created = "";	
    Day day = null;
    Till till = null;
    String reference = "";
    List<SaleDetailModel> saleDetails;
}
