/**
 * 
 */
package com.orbix.api.api;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orbix.api.domain.User;
import com.orbix.api.service.DayService;
import com.orbix.api.service.UserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class DayResource {
	
	private final DayService dayService;
	
	@GetMapping("/days/get_bussiness_date")
	public DayData getBussinessDate(){
		DayData dayData = new DayData();
		dayData.setBussinessDate(dayService.getBussinessDate());
		//return ResponseEntity.ok().body(dayService.getBussinessDate());
		return dayData;
	}
}
@Data
class DayData{
	public String bussinessDate;	
}
