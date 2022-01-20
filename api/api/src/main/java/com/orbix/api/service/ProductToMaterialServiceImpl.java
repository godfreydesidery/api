/**
 * 
 */
package com.orbix.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Customer;
import com.orbix.api.domain.Lpo;
import com.orbix.api.domain.Material;
import com.orbix.api.domain.MaterialStockCard;
import com.orbix.api.domain.Product;
import com.orbix.api.domain.ProductStockCard;
import com.orbix.api.domain.ProductToMaterial;
import com.orbix.api.domain.ProductToMaterialDetail;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.ProductToMaterialDetailModel;
import com.orbix.api.models.ProductToMaterialModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.MaterialRepository;
import com.orbix.api.repositories.ProductRepository;
import com.orbix.api.repositories.ProductToMaterialDetailRepository;
import com.orbix.api.repositories.ProductToMaterialRepository;
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
public class ProductToMaterialServiceImpl implements ProductToMaterialService{
	private final ProductToMaterialRepository productToMaterialRepository;
	private final ProductToMaterialDetailRepository productToMaterialDetailRepository;
	private final UserRepository userRepository;
	private final DayRepository dayRepository;
	private final ProductRepository productRepository;
	private final ProductStockCardService productStockCardService;
	private final MaterialStockCardService materialStockCardService;
	private final MaterialRepository materialRepository;
	
	@Override
	public ProductToMaterialModel save(ProductToMaterial productToMaterial) {
		if(!validate(productToMaterial)) {
			throw new InvalidEntryException("Could not save, Conversion invalid");
		}
		ProductToMaterial ptm = productToMaterialRepository.save(productToMaterial);
		if(ptm.getNo().equals("NA")) {
			ptm.setNo(generateProductToMaterialNo(ptm));
			ptm = productToMaterialRepository.save(ptm);
		}			
		ProductToMaterialModel model = new ProductToMaterialModel();
		model.setId(ptm.getId());
		model.setNo(ptm.getNo());
		model.setStatus(ptm.getStatus());
		model.setComments(ptm.getComments());
		if(ptm.getCreatedAt() != null && ptm.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(ptm.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(ptm.getCreatedBy()));
		}
		if(ptm.getApprovedAt() != null && ptm.getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(ptm.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(ptm.getApprovedBy()));
		}		
		return model;
	}
	
	@Override
	public ProductToMaterialModel get(Long id) {
		ProductToMaterialModel model = new ProductToMaterialModel();
		Optional<ProductToMaterial> ptm = productToMaterialRepository.findById(id);
		if(!ptm.isPresent()) {
			throw new NotFoundException("ProductToMaterial not found");
		}
		model.setId(ptm.get().getId());
		model.setNo(ptm.get().getNo());
		model.setStatus(ptm.get().getStatus());
		model.setComments(ptm.get().getComments());
		if(ptm.get().getCreatedAt() != null && ptm.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(ptm.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(ptm.get().getCreatedBy()));
		}
		if(ptm.get().getApprovedAt() != null && ptm.get().getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(ptm.get().getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(ptm.get().getApprovedBy()));
		}		
		List<ProductToMaterialDetail> productToMaterialDetails = ptm.get().getProductToMaterialDetails();
		List<ProductToMaterialDetailModel> modelDetails = new ArrayList<ProductToMaterialDetailModel>();
		for(ProductToMaterialDetail d : productToMaterialDetails) {
			ProductToMaterialDetailModel modelDetail = new ProductToMaterialDetailModel();
			modelDetail.setId(d.getId());
			modelDetail.setProduct(d.getProduct());
			modelDetail.setMaterial(d.getMaterial());
			modelDetail.setQty(d.getQty());
			modelDetail.setRatio(d.getRatio());
			modelDetail.setProductToMaterial(d.getProductToMaterial());
			modelDetails.add(modelDetail);
		}
		model.setProductToMaterialDetails(modelDetails);
		return model;
	}
	
	@Override
	public ProductToMaterialModel getByNo(String no) {
		ProductToMaterialModel model = new ProductToMaterialModel();
		Optional<ProductToMaterial> ptm = productToMaterialRepository.findByNo(no);
		if(!ptm.isPresent()) {
			throw new NotFoundException("ProductToMaterial not found");
		}
		model.setId(ptm.get().getId());
		model.setNo(ptm.get().getNo());
		model.setStatus(ptm.get().getStatus());
		model.setComments(ptm.get().getComments());
		if(ptm.get().getCreatedAt() != null && ptm.get().getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(ptm.get().getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(ptm.get().getCreatedBy()));
		}
		if(ptm.get().getApprovedAt() != null && ptm.get().getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(ptm.get().getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(ptm.get().getApprovedBy()));
		}		
		List<ProductToMaterialDetail> productToMaterialDetails = ptm.get().getProductToMaterialDetails();
		List<ProductToMaterialDetailModel> modelDetails = new ArrayList<ProductToMaterialDetailModel>();
		for(ProductToMaterialDetail d : productToMaterialDetails) {
			ProductToMaterialDetailModel modelDetail = new ProductToMaterialDetailModel();
			modelDetail.setId(d.getId());
			modelDetail.setProduct(d.getProduct());
			modelDetail.setMaterial(d.getMaterial());
			modelDetail.setQty(d.getQty());
			modelDetail.setRatio(d.getRatio());
			modelDetail.setProductToMaterial(d.getProductToMaterial());
			modelDetails.add(modelDetail);
		}
		model.setProductToMaterialDetails(modelDetails);
		return model;
	}
	
	@Override
	public boolean delete(ProductToMaterial productToMaterial) {
		if(!allowDelete(productToMaterial)) {
			throw new InvalidOperationException("Deleting the selected Conversion is not allowed");
		}
		productToMaterialRepository.delete(productToMaterial);
		return true;
	}
	
	@Override
	public List<ProductToMaterialModel> getAllVisible() {
		List<String> statuses = new ArrayList<String>();
		statuses.add("BLANK");
		statuses.add("PENDING");
		statuses.add("APPROVED");
		List<ProductToMaterial> ptms = productToMaterialRepository.findAllVissible(statuses);
		List<ProductToMaterialModel> models = new ArrayList<ProductToMaterialModel>();
		for(ProductToMaterial ptm : ptms) {
			ProductToMaterialModel model = new ProductToMaterialModel();
			model.setId(ptm.getId());
			model.setNo(ptm.getNo());
			model.setStatus(ptm.getStatus());
			model.setComments(ptm.getComments());
			if(ptm.getCreatedAt() != null && ptm.getCreatedBy() != null) {
				model.setCreated(dayRepository.findById(ptm.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(ptm.getCreatedBy()));
			}
			if(ptm.getApprovedAt() != null && ptm.getApprovedBy() != null) {
				model.setApproved(dayRepository.findById(ptm.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(ptm.getApprovedBy()));
			}			
			models.add(model);
		}
		return models;
	}
		
	@Override
	public ProductToMaterialDetailModel saveDetail(ProductToMaterialDetail productToMaterialDetail) {
		if(!validateDetail(productToMaterialDetail)) {
			throw new InvalidEntryException("Could not save detail, Invalid entry");
		}
		ProductToMaterialDetailModel model = new ProductToMaterialDetailModel();
		ProductToMaterialDetail d = productToMaterialDetailRepository.save(productToMaterialDetail);
		model.setId(d.getId());
		model.setProduct(d.getProduct());
		model.setMaterial(d.getMaterial());
		model.setQty(d.getQty());
		model.setRatio(d.getRatio());
		model.setProductToMaterial(d.getProductToMaterial());
		return model;
	}
	
	@Override
	public ProductToMaterialDetailModel getDetail(Long id) {
		ProductToMaterialDetailModel model = new ProductToMaterialDetailModel();
		Optional<ProductToMaterialDetail> d = productToMaterialDetailRepository.findById(id);
		if(!d.isPresent()) {
			throw new NotFoundException("ProductToMaterial detail not found");
		}
		model.setId(d.get().getId());
		model.setProduct(d.get().getProduct());
		model.setMaterial(d.get().getMaterial());
		model.setQty(d.get().getQty());
		model.setRatio(d.get().getRatio());
		model.setProductToMaterial(d.get().getProductToMaterial());
		return model;
	}
	
	@Override
	public boolean deleteDetail(ProductToMaterialDetail productToMaterialDetail) {
		if(!allowDeleteDetail(productToMaterialDetail)) {
			throw new InvalidOperationException("Deleting the selected Conversion detail is not allowed");
		}
		productToMaterialDetailRepository.delete(productToMaterialDetail);
		return true;
	}
	
	@Override
	public List<ProductToMaterialDetailModel> getAllDetails(ProductToMaterial productToMaterial) {
		List<ProductToMaterialDetail> details = productToMaterialDetailRepository.findByProductToMaterial(productToMaterial);
		List<ProductToMaterialDetailModel> models = new ArrayList<ProductToMaterialDetailModel>();
		for(ProductToMaterialDetail d : details) {
			ProductToMaterialDetailModel model = new ProductToMaterialDetailModel();
			model.setId(d.getId());
			model.setProduct(d.getProduct());
			model.setMaterial(d.getMaterial());
			model.setQty(d.getQty());
			model.setRatio(d.getRatio());
			model.setProductToMaterial(d.getProductToMaterial());
			models.add(model);
		}
		return models;	
	}
	
	@Override
	public boolean archive(ProductToMaterial productToMaterial) {
		if(!productToMaterial.getStatus().equals("APPROVED")) {
			throw new InvalidOperationException("Could not process, only an approved Conversion can be archived");
		}
		productToMaterial.setStatus("ARCHIVED");
		productToMaterialRepository.saveAndFlush(productToMaterial);
		return true;
	}
	
	@Override
	public boolean archiveAll() {
		List<ProductToMaterial> ptms = productToMaterialRepository.findAllApproved("APPROVED");
		if(ptms.isEmpty()) {
			throw new NotFoundException("No Document to archive");
		}
		for(ProductToMaterial p : ptms) {
			p.setStatus("ARCHIVED");
			productToMaterialRepository.saveAndFlush(p);
		}
		return true;
	}	
	
	private boolean validate(ProductToMaterial productToMaterial) {
		return true;
	}
	
	private boolean allowDelete(ProductToMaterial productToMaterial) {
		return true;
	}
	
	private boolean validateDetail(ProductToMaterialDetail productToMaterialDetail) {
		return true;
	}
	
	private boolean allowDeleteDetail(ProductToMaterialDetail productToMaterialDetail) {
		return true;
	}
	
	private String generateProductToMaterialNo(ProductToMaterial productToMaterial) {
		Long number = productToMaterial.getId();		
		String sNumber = number.toString();
		return "PTM-"+Formater.formatSix(sNumber);
	}

	@Override
	public ProductToMaterialModel post(ProductToMaterial productToMaterial) {
		/**
		 * Save invoice
		 * Deduct products from stock
		 * Update stock cards
		 */
		ProductToMaterial ptm = productToMaterialRepository.saveAndFlush(productToMaterial);
		List<ProductToMaterialDetail> details = ptm.getProductToMaterialDetails();
		for(ProductToMaterialDetail d : details) {
			/**
			 * Update stocks
			 * Create stock card
			 * First, take initial stock value
			 * Update stock
			 * Add qty to initial stock value to obtain final stock value
			 * Create stock card with the final stock value
			 */
			/**
			 * Here, update stock card
			 */
			Product product =productRepository.findById(d.getProduct().getId()).get();
			double productStock = product.getStock() - d.getQty();
			product.setStock(productStock);
			productRepository.saveAndFlush(product);
			
			Material material =materialRepository.findById(d.getMaterial().getId()).get();
			double materialStock = material.getStock() + (d.getQty() * d.getRatio());
			material.setStock(materialStock);
			materialRepository.saveAndFlush(material);
			/**
			 * Now create stock card
			 */
			ProductStockCard productStockCard = new ProductStockCard();
			productStockCard.setQtyOut(d.getQty());
			productStockCard.setProduct(product);
			productStockCard.setBalance(productStock);
			productStockCard.setDay(dayRepository.getCurrentBussinessDay());
			productStockCard.setReference("Used in Product to Material conversion. Ref #: "+ptm.getNo());
			productStockCardService.save(productStockCard);
			
			MaterialStockCard materialstockCard = new MaterialStockCard();
			materialstockCard.setQtyIn(d.getQty() * d.getRatio());
			materialstockCard.setMaterial(d.getMaterial());
			materialstockCard.setBalance(materialStock);
			materialstockCard.setDay(dayRepository.getCurrentBussinessDay());
			materialstockCard.setReference("Produced from Product to Material conversion. Ref #: "+ptm.getNo());
			materialStockCardService.save(materialstockCard);
			
		}
		ptm = productToMaterialRepository.saveAndFlush(ptm);
		ProductToMaterialModel model = new ProductToMaterialModel();
		model.setId(ptm.getId());
		model.setNo(ptm.getNo());
		model.setStatus(ptm.getStatus());
		model.setComments(ptm.getComments());
		if(ptm.getCreatedAt() != null && ptm.getCreatedBy() != null) {
			model.setCreated(dayRepository.findById(ptm.getCreatedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(ptm.getCreatedBy()));
		}
		if(ptm.getApprovedAt() != null && ptm.getApprovedBy() != null) {
			model.setApproved(dayRepository.findById(ptm.getApprovedAt()).get().getBussinessDate() +" "+ userRepository.getAlias(ptm.getApprovedBy()));
		}		
		return model;
	}

	@Override
	public List<ProductToMaterialModel> getByCustomerAndApprovedOrPartial(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}
}
