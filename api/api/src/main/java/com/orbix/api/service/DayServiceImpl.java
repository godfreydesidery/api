/**
 * 
 */
package com.orbix.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.orbix.api.domain.Day;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.NotFoundException;
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

}
