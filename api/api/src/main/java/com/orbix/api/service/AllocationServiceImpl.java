/**
 * 
 */
package com.orbix.api.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Allocation;
import com.orbix.api.domain.Customer;
import com.orbix.api.domain.SalesInvoice;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.AllocationRepository;
import com.orbix.api.repositories.CompanyProfileRepository;
import com.orbix.api.repositories.CustomerRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.SalesInvoiceRepository;

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
public class AllocationServiceImpl implements AllocationService {
	
	private final AllocationRepository allocationRepository;
	private final CustomerRepository customerRepository;
	private final SalesInvoiceRepository salesInvoiceRepository;
	private final UserService userService;
	private final DayService dayService;
	private final DayRepository dayRepository;

	@Override
	public boolean allocate(Customer customer, SalesInvoice salesInvoice, HttpServletRequest request) {
		Optional<Customer> c = customerRepository.findById(customer.getId());
		if(!c.isPresent()) {
			throw new NotFoundException("Customer not found in database");
		}
		Optional<SalesInvoice> i = salesInvoiceRepository.findById(salesInvoice.getId());
		if(!i.isPresent()) {
			throw new NotFoundException("Invouce not found in database");
		}
		if(c.get().getId() != i.get().getCustomer().getId()) {
			throw new InvalidOperationException("Customer and invoice do not match");
		}
		double customerBalance = customer.getBalance();
		double invoiceBalance = salesInvoice.getBalance();
		if(customerBalance <= 0) {
			throw new InvalidOperationException("Could not process, no balance available");
		}
		if(invoiceBalance <= 0) {
			throw new InvalidOperationException("Could not process, invalid invoice amount");
		}
		double allocationAmount = 0;
		if(customerBalance >= invoiceBalance) {
			double newCustomerBalance = customerBalance - invoiceBalance;
			double newInvoiceBalance = 0;
			allocationAmount = invoiceBalance;
			customer.setBalance(newCustomerBalance);
			salesInvoice.setBalance(newInvoiceBalance);
			customerRepository.saveAndFlush(customer);
			salesInvoiceRepository.saveAndFlush(salesInvoice);
		}else if(customerBalance < invoiceBalance) {
			double newCustomerBalance = 0;
			double newInvoiceBalance = invoiceBalance - customerBalance;
			allocationAmount = customerBalance;
			customer.setBalance(newCustomerBalance);
			salesInvoice.setBalance(newInvoiceBalance);
			customerRepository.saveAndFlush(customer);
			salesInvoiceRepository.saveAndFlush(salesInvoice);
		}
		Allocation allocation = new Allocation();
		allocation.setNo("NA");
		allocation.setSalesInvoice(salesInvoice);
		allocation.setAmount(allocationAmount);
		allocation.setCreatedBy(userService.getUserId(request));
		allocation.setCreatedAt(dayRepository.getCurrentBussinessDay().getId());
		Allocation a = allocationRepository.saveAndFlush(allocation);
		if(a.getNo().equals("NA")) {
			a.setNo(generateAllocationNo(a));
			a = allocationRepository.saveAndFlush(a);
		}
		return true;		
	}
	
	private String generateAllocationNo(Allocation allocation) {
		Long number = allocation.getId();		
		String sNumber = number.toString();
		return "ALC-"+Formater.formatSix(sNumber);
	}

}
