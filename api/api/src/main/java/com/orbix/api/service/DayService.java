/**
 * 
 */
package com.orbix.api.service;

import java.time.LocalDate;
import java.util.Date;

import com.orbix.api.domain.Day;

/**
 * @author GODFREY
 *
 */
public interface DayService {
	Day saveDay(Day day);
	String getBussinessDate();
	
}
