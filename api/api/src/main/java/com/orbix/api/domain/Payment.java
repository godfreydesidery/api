/**
 * 
 */
package com.orbix.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GODFREY
 *
 */
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class Payment {
	double cash;
	double cheque;
	double voucher;
	double deposit;
	double loyalty;
	double crCard;
	double cap;
	double invoice;
	double crNote;
	double mobile;
	double other;
}
