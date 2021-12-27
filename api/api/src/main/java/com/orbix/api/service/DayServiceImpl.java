/**
 * 
 */
package com.orbix.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Day;
import com.orbix.api.repositories.DayRepository;
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
public class DayServiceImpl implements DayService{
	
	private final DayRepository dayRepository;
	private final ProductStockService productStockService;
	private final ProductService productService;
	private final MaterialStockService materialStockService;
	private final MaterialService materialService;
	
	@Override
	public String getBussinessDate() {		
		Day day = dayRepository.getCurrentBussinessDay();
		return day.getBussinessDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	@Override
	public Day saveDay(Day day) {
		// TODO Auto-generated method stub
		return dayRepository.save(day);
	}

	@Override
	public boolean hasData() {
		return dayRepository.hasData();
	}

	@Override
	public boolean endDay() {
		// TODO Auto-generated method stub		
		log.info("Ending the day");
		Day oldDay = dayRepository.getCurrentBussinessDay();
		Day newDay = new Day();
		oldDay.setEndedAt(new Date());
		oldDay.setStatus("ENDED");
		dayRepository.saveAndFlush(oldDay);
		newDay.setStartedAt(new Date());
		newDay.setBussinessDate(oldDay.getBussinessDate());
		if(newDay.getBussinessDate().equals(LocalDate.now())) {
			newDay.setBussinessDate(LocalDate.now().plusDays(1));
		}else if(newDay.getBussinessDate().isAfter(LocalDate.now())) {
			//do nothing
		}else if(newDay.getBussinessDate().isBefore(LocalDate.now())) {
			newDay.setBussinessDate(LocalDate.now());
		}
		dayRepository.saveAndFlush(newDay);
		log.info("Closing product stock");
		productStockService.closeStock(productService.getAll(), oldDay);
		log.info("Opening product stock");
		productStockService.openStock(productService.getAll(), newDay);
		log.info("Closing material stock");
		materialStockService.closeStock(materialService.getAll(), oldDay);
		log.info("Opening material stock");
		materialStockService.openStock(materialService.getAll(), newDay);
		return true;
	}

	@Override
	public Long getDayId() {
		return dayRepository.getLastId();
	}

}
