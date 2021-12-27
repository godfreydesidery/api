/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.domain.Department;
import com.orbix.api.domain.LevelFour;
import com.orbix.api.domain.LevelOne;
import com.orbix.api.domain.LevelThree;
import com.orbix.api.domain.LevelTwo;
import com.orbix.api.domain.SubCategory;
import com.orbix.api.domain.SubClass;
import com.orbix.api.service.UnitService;
import com.orbix.api.domain.Category;
import com.orbix.api.domain.Class;

import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UnitResource {
	
	private final 	UnitService unitService;
	
	@GetMapping("/departments")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<List<Department>>getDepartments(){
		return ResponseEntity.ok().body(unitService.getDepartments());
	}
	
	@GetMapping("/departments/get")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<Department> getDepartment(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(unitService.getDepartment(id));
	}
	
	@GetMapping("/departments/get_by_name")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<Department> getDepartmentByName(
			@RequestParam(name = "name") String name){
		return ResponseEntity.ok().body(unitService.getDepartmentByName(name));
	}
	
	@PostMapping("/departments/create")
	//@PreAuthorize("hasAnyAuthority('TILL-CREATE')")
	public ResponseEntity<Department>createDepartment(
			@RequestBody Department department){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/departments/create").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveDepartment(department));
	}
		
	@PutMapping("/departments/update")
	//@PreAuthorize("hasAnyAuthority('USER-UPDATE')")
	public ResponseEntity<Department>updateDepartment(
			@RequestBody Department department, 
			HttpServletRequest request){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/departments/update").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveDepartment(department));
	}
	
	@DeleteMapping("/departments/delete")
	//@PreAuthorize("hasAnyAuthority('TILL-DELETE')")
	public ResponseEntity<Boolean> deleteDepartment(
			@RequestParam(name = "id") Long id){
		Department department = unitService.getDepartment(id);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/departments/delete").toUriString());
		return ResponseEntity.created(uri).body(unitService.deleteDepartment(department));
	}
	
	
	@GetMapping("/classes")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<List<Class>>getclasses(){
		return ResponseEntity.ok().body(unitService.getClasses());
	}
	
	@GetMapping("/classes/get")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<Class> getClass(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(unitService.getClass(id));
	}
	
	@GetMapping("/classes/get_by_name")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<Class> getClassByName(
			@RequestParam(name = "name") String name){
		return ResponseEntity.ok().body(unitService.getClassByName(name));
	}
	
	@GetMapping("/classes/get_by_department_name")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<List<Class>> getClassByDepartmentName(
			@RequestParam(name = "department_name") String departmentName){
		Department d = unitService.getDepartmentByName(departmentName);
		List<Class> cs = unitService.getClasses();
		List<Class> cr = new ArrayList<Class>();
		for(Class c : cs) {
			if(c.getDepartment().equals(d)) {
				cr.add(c);
			}
		}
		
		return ResponseEntity.ok().body(cr);
	}
	
	@PostMapping("/classes/create")
	//@PreAuthorize("hasAnyAuthority('TILL-CREATE')")
	public ResponseEntity<Class>createClass(
			@RequestBody Class class_){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/classes/create").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveClass(class_));
	}
		
	@PutMapping("/classes/update")
	//@PreAuthorize("hasAnyAuthority('USER-UPDATE')")
	public ResponseEntity<Class>updateClass(
			@RequestBody Class class_, 
			HttpServletRequest request){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/classes/update").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveClass(class_));
	}
	
	@DeleteMapping("/classes/delete")
	//@PreAuthorize("hasAnyAuthority('TILL-DELETE')")
	public ResponseEntity<Boolean> deleteClass(
			@RequestParam(name = "id") Long id){
		Class class_ = unitService.getClass(id);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/classes/delete").toUriString());
		return ResponseEntity.created(uri).body(unitService.deleteClass(class_));
	}
	
	
	@GetMapping("/sub_classes")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<List<SubClass>>getSubClasss(){
		return ResponseEntity.ok().body(unitService.getSubClasses());
	}
	
	@GetMapping("/sub_classes/get")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<SubClass> getSubClass(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(unitService.getSubClass(id));
	}
	
	@GetMapping("/sub_classes/get_by_name")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<SubClass> getSubClassByName(
			@RequestParam(name = "name") String name){
		return ResponseEntity.ok().body(unitService.getSubClassByName(name));
	}
	
	@GetMapping("/sub_classes/get_by_class_name")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<List<SubClass>> getSubClassByClassName(
			@RequestParam(name = "class_name") String className){
		Class d = unitService.getClassByName(className);
		List<SubClass> cs = unitService.getSubClasses();
		List<SubClass> cr = new ArrayList<SubClass>();
		for(SubClass c : cs) {
			if(c.getClass_().equals(d)) {
				cr.add(c);
			}
		}
		
		return ResponseEntity.ok().body(cr);
	}
	
	@PostMapping("/sub_classes/create")
	//@PreAuthorize("hasAnyAuthority('TILL-CREATE')")
	public ResponseEntity<SubClass>createSubClass(
			@RequestBody SubClass subClass){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sub_classes/create").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveSubClass(subClass));
	}
		
	@PutMapping("/sub_classes/update")
	//@PreAuthorize("hasAnyAuthority('USER-UPDATE')")
	public ResponseEntity<SubClass>updateSubClass(
			@RequestBody SubClass subClass, 
			HttpServletRequest request){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sub_classes/update").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveSubClass(subClass));
	}
	
	@DeleteMapping("/sub_classes/delete")
	//@PreAuthorize("hasAnyAuthority('TILL-DELETE')")
	public ResponseEntity<Boolean> deleteSubClass(
			@RequestParam(name = "id") Long id){
		SubClass subClass = unitService.getSubClass(id);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sub_classes/delete").toUriString());
		return ResponseEntity.created(uri).body(unitService.deleteSubClass(subClass));
	}
	
	
	
	@GetMapping("/categories")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<List<Category>>getCategorys(){
		return ResponseEntity.ok().body(unitService.getCategories());
	}
	
	@GetMapping("/categories/get")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<Category> getCategory(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(unitService.getCategory(id));
	}
	
	@GetMapping("/categories/get_by_name")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<Category> getCategoryByName(
			@RequestParam(name = "name") String name){
		return ResponseEntity.ok().body(unitService.getCategoryByName(name));
	}
	
	@PostMapping("/categories/create")
	//@PreAuthorize("hasAnyAuthority('TILL-CREATE')")
	public ResponseEntity<Category>createCategory(
			@RequestBody Category category){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/categories/create").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveCategory(category));
	}
		
	@PutMapping("/categories/update")
	//@PreAuthorize("hasAnyAuthority('USER-UPDATE')")
	public ResponseEntity<Category>updateCategory(
			@RequestBody Category category, 
			HttpServletRequest request){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/categories/update").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveCategory(category));
	}
	
	@DeleteMapping("/categories/delete")
	//@PreAuthorize("hasAnyAuthority('TILL-DELETE')")
	public ResponseEntity<Boolean> deleteCategory(
			@RequestParam(name = "id") Long id){
		Category category = unitService.getCategory(id);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/categories/delete").toUriString());
		return ResponseEntity.created(uri).body(unitService.deleteCategory(category));
	}
	
	@GetMapping("/sub_categories")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<List<SubCategory>>getSubCategorys(){
		return ResponseEntity.ok().body(unitService.getSubCategories());
	}
	
	@GetMapping("/sub_categories/get")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<SubCategory> getSubCategory(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(unitService.getSubCategory(id));
	}
	
	@GetMapping("/sub_categories/get_by_name")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<SubCategory> getSubCategoryByName(
			@RequestParam(name = "name") String name){
		return ResponseEntity.ok().body(unitService.getSubCategoryByName(name));
	}
	
	@GetMapping("/sub_categories/get_by_category_name")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<List<SubCategory>> getSubCategoryByCategoryName(
			@RequestParam(name = "category_name") String categoryName){
		Category d = unitService.getCategoryByName(categoryName);
		List<SubCategory> cs = unitService.getSubCategories();
		List<SubCategory> cr = new ArrayList<SubCategory>();
		for(SubCategory c : cs) {
			if(c.getCategory().equals(d)) {
				cr.add(c);
			}
		}
		
		return ResponseEntity.ok().body(cr);
	}
	
	@PostMapping("/sub_categories/create")
	//@PreAuthorize("hasAnyAuthority('TILL-CREATE')")
	public ResponseEntity<SubCategory>createSubCategory(
			@RequestBody SubCategory subCategory){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sub_categories/create").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveSubCategory(subCategory));
	}
		
	@PutMapping("/sub_categories/update")
	//@PreAuthorize("hasAnyAuthority('USER-UPDATE')")
	public ResponseEntity<SubCategory>updateSubCategory(
			@RequestBody SubCategory subCategory, 
			HttpServletRequest request){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sub_categories/update").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveSubCategory(subCategory));
	}
	
	@DeleteMapping("/sub_categories/delete")
	//@PreAuthorize("hasAnyAuthority('TILL-DELETE')")
	public ResponseEntity<Boolean> deleteSubCategory(
			@RequestParam(name = "id") Long id){
		SubCategory subCategory = unitService.getSubCategory(id);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/sub_categories/delete").toUriString());
		return ResponseEntity.created(uri).body(unitService.deleteSubCategory(subCategory));
	}
	
	
	
	@GetMapping("/group_level_ones")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<List<LevelOne>>getLevelOnes(){
		return ResponseEntity.ok().body(unitService.getLevelOnes());
	}
	
	@GetMapping("/group_level_ones/get")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<LevelOne> getLevelOne(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(unitService.getLevelOne(id));
	}
	
	@GetMapping("/group_level_ones/get_by_name")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<LevelOne> getLevelOneByName(
			@RequestParam(name = "name") String name){
		return ResponseEntity.ok().body(unitService.getLevelOneByName(name));
	}
	
	@PostMapping("/group_level_ones/create")
	//@PreAuthorize("hasAnyAuthority('TILL-CREATE')")
	public ResponseEntity<LevelOne>createLevelOne(
			@RequestBody LevelOne levelOne){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/group_level_ones/create").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveLevelOne(levelOne));
	}
		
	@PutMapping("/group_level_ones/update")
	//@PreAuthorize("hasAnyAuthority('USER-UPDATE')")
	public ResponseEntity<LevelOne>updateLevelOne(
			@RequestBody LevelOne levelOne, 
			HttpServletRequest request){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/group_level_ones/update").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveLevelOne(levelOne));
	}
	
	@DeleteMapping("/group_level_ones/delete")
	//@PreAuthorize("hasAnyAuthority('TILL-DELETE')")
	public ResponseEntity<Boolean> deleteLevelOne(
			@RequestParam(name = "id") Long id){
		LevelOne levelOne = unitService.getLevelOne(id);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/group_level_ones/delete").toUriString());
		return ResponseEntity.created(uri).body(unitService.deleteLevelOne(levelOne));
	}
	
	
	
	
	
	@GetMapping("/group_level_twos")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<List<LevelTwo>>getLevelTwos(){
		return ResponseEntity.ok().body(unitService.getLevelTwos());
	}
	
	@GetMapping("/group_level_twos/get")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<LevelTwo> getLevelTwo(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(unitService.getLevelTwo(id));
	}
	
	@GetMapping("/group_level_twos/get_by_name")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<LevelTwo> getLevelTwoByName(
			@RequestParam(name = "name") String name){
		return ResponseEntity.ok().body(unitService.getLevelTwoByName(name));
	}
	
	@PostMapping("/group_level_twos/create")
	//@PreAuthorize("hasAnyAuthority('TILL-CREATE')")
	public ResponseEntity<LevelTwo>createLevelTwo(
			@RequestBody LevelTwo levelTwo){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/group_level_twos/create").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveLevelTwo(levelTwo));
	}
		
	@PutMapping("/group_level_twos/update")
	//@PreAuthorize("hasAnyAuthority('USER-UPDATE')")
	public ResponseEntity<LevelTwo>updateLevelTwo(
			@RequestBody LevelTwo levelTwo, 
			HttpServletRequest request){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/group_level_twos/update").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveLevelTwo(levelTwo));
	}
	
	@DeleteMapping("/group_level_twos/delete")
	//@PreAuthorize("hasAnyAuthority('TILL-DELETE')")
	public ResponseEntity<Boolean> deleteLevelTwo(
			@RequestParam(name = "id") Long id){
		LevelTwo levelTwo = unitService.getLevelTwo(id);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/group_level_twos/delete").toUriString());
		return ResponseEntity.created(uri).body(unitService.deleteLevelTwo(levelTwo));
	}
	
	@GetMapping("/group_level_threes")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<List<LevelThree>>getLevelThrees(){
		return ResponseEntity.ok().body(unitService.getLevelThrees());
	}
	
	@GetMapping("/group_level_threes/get")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<LevelThree> getLevelThree(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(unitService.getLevelThree(id));
	}
	
	@GetMapping("/group_level_threes/get_by_name")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<LevelThree> getLevelThreeByName(
			@RequestParam(name = "name") String name){
		return ResponseEntity.ok().body(unitService.getLevelThreeByName(name));
	}
	
	@PostMapping("/group_level_threes/create")
	//@PreAuthorize("hasAnyAuthority('TILL-CREATE')")
	public ResponseEntity<LevelThree>createLevelThree(
			@RequestBody LevelThree levelThree){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/group_level_threes/create").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveLevelThree(levelThree));
	}
		
	@PutMapping("/group_level_threes/update")
	//@PreAuthorize("hasAnyAuthority('USER-UPDATE')")
	public ResponseEntity<LevelThree>updateLevelThree(
			@RequestBody LevelThree levelThree, 
			HttpServletRequest request){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/group_level_threes/update").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveLevelThree(levelThree));
	}
	
	@DeleteMapping("/group_level_threes/delete")
	//@PreAuthorize("hasAnyAuthority('TILL-DELETE')")
	public ResponseEntity<Boolean> deleteLevelThree(
			@RequestParam(name = "id") Long id){
		LevelThree levelThree = unitService.getLevelThree(id);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/group_level_threes/delete").toUriString());
		return ResponseEntity.created(uri).body(unitService.deleteLevelThree(levelThree));
	}
	
	
	
	@GetMapping("/group_level_fours")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<List<LevelFour>>getLevelFours(){
		return ResponseEntity.ok().body(unitService.getLevelFours());
	}
	
	@GetMapping("/group_level_fours/get")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<LevelFour> getLevelFour(
			@RequestParam(name = "id") Long id){
		return ResponseEntity.ok().body(unitService.getLevelFour(id));
	}
	
	@GetMapping("/group_level_fours/get_by_name")
	//@PreAuthorize("hasAnyAuthority('TILL-READ')")
	public ResponseEntity<LevelFour> getLevelFourByName(
			@RequestParam(name = "name") String name){
		return ResponseEntity.ok().body(unitService.getLevelFourByName(name));
	}
	
	@PostMapping("/group_level_fours/create")
	//@PreAuthorize("hasAnyAuthority('TILL-CREATE')")
	public ResponseEntity<LevelFour>createLevelFour(
			@RequestBody LevelFour levelFour){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/group_level_fours/create").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveLevelFour(levelFour));
	}
		
	@PutMapping("/group_level_fours/update")
	//@PreAuthorize("hasAnyAuthority('USER-UPDATE')")
	public ResponseEntity<LevelFour>updateLevelFour(
			@RequestBody LevelFour levelFour, 
			HttpServletRequest request){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/group_level_fours/update").toUriString());
		return ResponseEntity.created(uri).body(unitService.saveLevelFour(levelFour));
	}
	
	@DeleteMapping("/group_level_fours/delete")
	//@PreAuthorize("hasAnyAuthority('TILL-DELETE')")
	public ResponseEntity<Boolean> deleteLevelFour(
			@RequestParam(name = "id") Long id){
		LevelFour levelFour = unitService.getLevelFour(id);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/group_level_fours/delete").toUriString());
		return ResponseEntity.created(uri).body(unitService.deleteLevelFour(levelFour));
	}
	
}
