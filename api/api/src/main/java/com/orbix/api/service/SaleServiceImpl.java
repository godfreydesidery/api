/**
 * 
 */
package com.orbix.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Sale;
import com.orbix.api.domain.Sale;
import com.orbix.api.domain.SaleDetail;
import com.orbix.api.domain.SalesInvoice;
import com.orbix.api.domain.SalesInvoiceDetail;
import com.orbix.api.domain.Sale;
import com.orbix.api.domain.SaleDetail;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.SaleModel;
import com.orbix.api.models.SalesInvoiceDetailModel;
import com.orbix.api.models.SalesInvoiceModel;
import com.orbix.api.models.SaleDetailModel;
import com.orbix.api.models.SaleModel;
import com.orbix.api.models.SaleDetailModel;
import com.orbix.api.models.SaleModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.QuotationDetailRepository;
import com.orbix.api.repositories.QuotationRepository;
import com.orbix.api.repositories.SaleDetailRepository;
import com.orbix.api.repositories.SaleRepository;
import com.orbix.api.repositories.UserRepository;

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
public class SaleServiceImpl implements SaleService {
	
	private final SaleRepository saleRepository;
	private final SaleDetailRepository saleDetailRepository;
	private final UserRepository userRepository;
	private final DayRepository dayRepository;
	private final ProductRepository productRepository;
	private final ProductStockCardService productStockCardService;

	@Override
	public SaleModel save(Sale sale) {
		Sale s = saleRepository.save(sale);	
		SaleModel model = new SaleModel();
		model.setId(s.getId());
		model.setDay(s.getDay());
		model.setTill(s.getTill());
		if(s.getCreatedAt() != null && s.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(s.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(s.getCreatedBy()));
		}		
		return model;
	}

	@Override
	public SaleModel get(Long id) {
		SaleModel model = new SaleModel();
		Optional<Sale> sale = saleRepository.findById(id);
		if(!sale.isPresent()) {
			throw new NotFoundException("Sale not found");
		}
		model.setId(sale.get().getId());
		model.setDay(sale.get().getDay());
		model.setTill(sale.get().getTill());
		if(sale.get().getCreatedAt() != null && sale.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(sale.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(sale.get().getCreatedBy()));
		}		
		List<SaleDetail> saleDetails = sale.get().getSaleDetails();
		List<SaleDetailModel> modelDetails = new ArrayList<SaleDetailModel>();
		for(SaleDetail d : saleDetails) {
			SaleDetailModel modelDetail = new SaleDetailModel();
			modelDetail.setId(d.getId());
			modelDetail.setProduct(d.getProduct());
			modelDetail.setQty(d.getQty());
			modelDetail.setCostPriceVatIncl(d.getCostPriceVatIncl());
			modelDetail.setCostPriceVatExcl(d.getCostPriceVatExcl());
			modelDetail.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
			modelDetail.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
			modelDetail.setDiscount(d.getDiscount());
			modelDetail.setTax(d.getTax());
			modelDetail.setSale(d.getSale());
			modelDetails.add(modelDetail);
		}
		model.setSaleDetails(modelDetails);
		return model;
	}

	@Override
	public boolean delete(Sale sale) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SaleModel> getAll() {
		List<Sale> sales = saleRepository.findAll();
		List<SaleModel> models = new ArrayList<SaleModel>();
		for(Sale sale : sales) {
			SaleModel model = new SaleModel();
			model.setId(sale.getId());
			model.setDay(sale.getDay());
			model.setTill(sale.getTill());
			if(sale.getCreatedAt() != null && sale.getCreatedBy() != null) {
				model.setCreated(dayRepository.findById(sale.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(sale.getCreatedBy()));
			}	
			models.add(model);
		}
		return models;
	}

	@Override
	public SaleDetailModel saveDetail(SaleDetail saleDetail) {	
		SaleDetail d = saleDetailRepository.saveAndFlush(saleDetail);
		SaleDetailModel modelDetail = new SaleDetailModel();
		modelDetail.setId(d.getId());
		modelDetail.setProduct(d.getProduct());
		modelDetail.setQty(d.getQty());
		modelDetail.setCostPriceVatIncl(d.getCostPriceVatIncl());
		modelDetail.setCostPriceVatExcl(d.getCostPriceVatExcl());
		modelDetail.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
		modelDetail.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
		modelDetail.setDiscount(d.getDiscount());
		modelDetail.setTax(d.getTax());
		modelDetail.setSale(d.getSale());
		return modelDetail;
	}

	@Override
	public SaleDetailModel getDetail(Long id) {
		SaleDetailModel model = new SaleDetailModel();
		Optional<SaleDetail> d = saleDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("Sale detail not found");
		}
		model.setId(d.get().getId());
		model.setProduct(d.get().getProduct());
		model.setQty(d.get().getQty());
		model.setCostPriceVatIncl(d.get().getCostPriceVatIncl());
		model.setCostPriceVatExcl(d.get().getCostPriceVatExcl());
		model.setSellingPriceVatIncl(d.get().getSellingPriceVatIncl());
		model.setSellingPriceVatExcl(d.get().getSellingPriceVatExcl());
		model.setDiscount(d.get().getDiscount());
		model.setTax(d.get().getTax());
		model.setSale(d.get().getSale());
		return model;
	}

	@Override
	public boolean deleteDetail(SaleDetail saleDetail) {
		if(!allowDeleteDetail(saleDetail)) {
			throw new InvalidOperationException("Deleting the selected detail is not allowed");
		}
		saleDetailRepository.delete(saleDetail);
		return true;
	}

	@Override
	public List<SaleDetailModel> getAllDetails(Sale sale) {
		List<SaleDetail> details = saleDetailRepository.findBySale(sale);
		List<SaleDetailModel> models = new ArrayList<SaleDetailModel>();
		for(SaleDetail d : details) {
			SaleDetailModel model = new SaleDetailModel();
			model.setId(d.getId());
			model.setProduct(d.getProduct());
			model.setQty(d.getQty());
			model.setCostPriceVatIncl(d.getCostPriceVatIncl());
			model.setCostPriceVatExcl(d.getCostPriceVatExcl());
			model.setSellingPriceVatIncl(d.getSellingPriceVatIncl());
			model.setSellingPriceVatExcl(d.getSellingPriceVatExcl());
			model.setDiscount(d.getDiscount());
			model.setTax(d.getTax());
			model.setSale(d.getSale());
			models.add(model);
		}
		return models;	
	}
	
	private boolean validate(Sale sale) {
		return true;
	}
	
	private boolean allowDelete(Sale sale) {
		return true;
	}
	
	private boolean validateDetail(SaleDetail saleDetail) {
		return true;
	}
	
	private boolean allowDeleteDetail(SaleDetail saleDetail) {
		return true;
	}

}
