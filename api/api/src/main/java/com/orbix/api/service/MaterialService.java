/**
 * 
 */
package com.orbix.api.service;

import java.util.ArrayList;
import java.util.List;

import com.orbix.api.domain.Material;

/**
 * @author GODFREY
 *
 */
public interface MaterialService {
	ArrayList<String> getNames();
	Material save(Material material);
	Material get(Long id);
	Material getByCode(String code);
	Material getByDescription(String description);
	boolean delete(Material material);
	List<Material>getAll();	
	List<String> getActiveDescriptions();
}
