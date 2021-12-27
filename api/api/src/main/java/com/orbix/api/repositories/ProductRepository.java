/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Product;

/**
 * @author GODFREY
 *
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
	/**
	 * @param barcode
	 * @return
	 */
	Optional<Product> findByBarcode(String barcode);

	/**
	 * @param code
	 * @return
	 */
	Optional<Product> findByCode(String code);

	/**
	 * @param description
	 * @return
	 */
	Optional<Product> findByDescription(String description);

	/**
	 * @param commonName
	 * @return
	 */
	Optional<Product> findByCommonName(String commonName);
	
	@Query("SELECT p.description FROM Product p WHERE p.active =1 AND p.sellable=1")
	List<String> getSellableProductDescriptions();

}
