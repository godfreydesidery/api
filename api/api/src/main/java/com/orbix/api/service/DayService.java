/**
 * 
 */
package com.orbix.api.service;

import java.time.LocalDate;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Day;

/**
 * @author GODFREY
 *
 */
public interface DayService {
	Day saveDay(Day day);
	String getBussinessDate();
	boolean hasData();
	boolean endDay();
	
	Long getDayId();
	
}
