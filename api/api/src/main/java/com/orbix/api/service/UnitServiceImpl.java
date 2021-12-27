/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Category;
import com.orbix.api.domain.Class;
import com.orbix.api.domain.Department;
import com.orbix.api.domain.LevelFour;
import com.orbix.api.domain.LevelOne;
import com.orbix.api.domain.LevelThree;
import com.orbix.api.domain.LevelTwo;
import com.orbix.api.domain.SubCategory;
import com.orbix.api.domain.SubClass;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.CategoryRepository;
import com.orbix.api.repositories.ClassRepository;
import com.orbix.api.repositories.DepartmentRepository;
import com.orbix.api.repositories.LevelFourRepository;
import com.orbix.api.repositories.LevelOneRepository;
import com.orbix.api.repositories.LevelThreeRepository;
import com.orbix.api.repositories.LevelTwoRepository;
import com.orbix.api.repositories.SubCategoryRepository;
import com.orbix.api.repositories.SubClassRepository;
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
public class UnitServiceImpl implements UnitService {
	
	private final DepartmentRepository departmentRepository;
	private final ClassRepository classRepository;
	private final SubClassRepository subClassRepository;
	private final CategoryRepository categoryRepository;
	private final SubCategoryRepository subCategoryRepository;
	private final LevelOneRepository levelOneRepository;
	private final LevelTwoRepository levelTwoRepository;
	private final LevelThreeRepository levelThreeRepository;
	private final LevelFourRepository levelFourRepository;

	@Override
	public Department saveDepartment(Department department) {
		if(department.getId() != null) {
			Optional<Department> d = departmentRepository.findById(department.getId());
			if(!d.isPresent()) {
				throw new NotFoundException("Department not found");
			}else {
				List<Class> c = d.get().getClasses();
				department.setClasses(c);
			}
		}
		return departmentRepository.save(department);
	}

	@Override
	public Department getDepartment(Long id) {
		return departmentRepository.findById(id).get();
	}

	@Override
	public Department getDepartmentByName(String name) {
		Optional<Department> department = departmentRepository.findByName(name);
		if(!department.isPresent()) {
			throw new NotFoundException("Department not found");
		}
		return department.get();
	}

	@Override
	public boolean deleteDepartment(Department department) {
		departmentRepository.delete(department);
		return true;
	}

	@Override
	public List<Department> getDepartments() {
		return departmentRepository.findAll();
	}

	@Override
	public Class saveClass(Class class_) {
		if(class_.getId() != null) {
			Optional<Class> c = classRepository.findById(class_.getId());
			if(!c.isPresent()) {
				throw new NotFoundException("Class not found");
			}else {
				List<SubClass> sc = c.get().getSubClasses();
				class_.setSubClasses(sc);
			}
		}
		
		Optional<Department> d = departmentRepository.findByName(class_.getDepartment().getName());
		if(!d.isPresent()) {
			throw new NotFoundException("Department not found");
		}
		class_.setDepartment(d.get());
		return classRepository.save(class_);
	}

	@Override
	public Class getClass(Long id) {
		return classRepository.findById(id).get();
	}

	@Override
	public Class getClassByName(String name) {
		Optional<Class> class_ = classRepository.findByName(name);
		if(!class_.isPresent()) {
			throw new NotFoundException("Class not found");
		}
		return class_.get();
	}

	@Override
	public boolean deleteClass(Class class_) {
		classRepository.delete(class_);
		return true;
	}

	@Override
	public List<Class> getClasses() {
		return classRepository.findAll();
	}

	@Override
	public SubClass saveSubClass(SubClass subClass) {
		Optional<Class> c = classRepository.findByName(subClass.getClass_().getName());
		System.out.println(subClass.getClass_().getName());
		if(!c.isPresent()) {
			throw new NotFoundException("Class not found");
		}
		subClass.setClass_(c.get());
		return subClassRepository.save(subClass);
	}

	@Override
	public SubClass getSubClass(Long id) {
		return subClassRepository.findById(id).get();
	}

	@Override
	public SubClass getSubClassByName(String name) {
		Optional<SubClass> subClass = subClassRepository.findByName(name);
		if(!subClass.isPresent()) {
			throw new NotFoundException("Sub Class not found");
		}
		return subClass.get();
	}

	@Override
	public boolean deleteSubClass(SubClass subClass) {
		subClassRepository.delete(subClass);
		return true;
	}

	@Override
	public List<SubClass> getSubClasses() {
		return subClassRepository.findAll();
	}

	@Override
	public LevelOne saveLevelOne(LevelOne levelOne) {
		return levelOneRepository.save(levelOne);
	}

	@Override
	public LevelOne getLevelOne(Long id) {
		return levelOneRepository.findById(id).get();
	}

	@Override
	public LevelOne getLevelOneByName(String name) {
		Optional<LevelOne> levelOne = levelOneRepository.findByName(name);
		if(!levelOne.isPresent()) {
			throw new NotFoundException("Group not found");
		}
		return levelOne.get();
	}

	@Override
	public boolean deleteLevelOne(LevelOne levelOne) {
		levelOneRepository.delete(levelOne);
		return true;
	}

	@Override
	public List<LevelOne> getLevelOnes() {
		return levelOneRepository.findAll();
	}

	@Override
	public LevelTwo saveLevelTwo(LevelTwo levelTwo) {
		return levelTwoRepository.save(levelTwo);
	}

	@Override
	public LevelTwo getLevelTwo(Long id) {
		return levelTwoRepository.findById(id).get();
	}

	@Override
	public LevelTwo getLevelTwoByName(String name) {
		Optional<LevelTwo> levelTwo = levelTwoRepository.findByName(name);
		if(!levelTwo.isPresent()) {
			throw new NotFoundException("Group not found");
		}
		return levelTwo.get();
	}

	@Override
	public boolean deleteLevelTwo(LevelTwo levelTwo) {
		levelTwoRepository.delete(levelTwo);
		return true;
	}

	@Override
	public List<LevelTwo> getLevelTwos() {
		return levelTwoRepository.findAll();
	}

	@Override
	public LevelThree saveLevelThree(LevelThree levelThree) {
		return levelThreeRepository.save(levelThree);
	}

	@Override
	public LevelThree getLevelThree(Long id) {
		return levelThreeRepository.findById(id).get();
	}

	@Override
	public LevelThree getLevelThreeByName(String name) {
		Optional<LevelThree> levelThree = levelThreeRepository.findByName(name);
		if(!levelThree.isPresent()) {
			throw new NotFoundException("Group not found");
		}
		return levelThree.get();
	}

	@Override
	public boolean deleteLevelThree(LevelThree levelThree) {
		levelThreeRepository.delete(levelThree);
		return true;
	}

	@Override
	public List<LevelThree> getLevelThrees() {
		return levelThreeRepository.findAll();
	}

	@Override
	public LevelFour saveLevelFour(LevelFour levelFour) {
		return levelFourRepository.save(levelFour);
	}

	@Override
	public LevelFour getLevelFour(Long id) {
		return levelFourRepository.findById(id).get();
	}

	@Override
	public LevelFour getLevelFourByName(String name) {
		Optional<LevelFour> levelFour = levelFourRepository.findByName(name);
		if(!levelFour.isPresent()) {
			throw new NotFoundException("Group not found");
		}
		return levelFour.get();
	}

	@Override
	public boolean deleteLevelFour(LevelFour levelFour) {
		levelFourRepository.delete(levelFour);
		return true;
	}

	@Override
	public List<LevelFour> getLevelFours() {
		return levelFourRepository.findAll();
	}

	@Override
	public Category saveCategory(Category category) {
		if(category.getId() != null) {
			Optional<Category> c = categoryRepository.findById(category.getId());
			if(!c.isPresent()) {
				throw new NotFoundException("Category not found");
			}else {
				List<SubCategory> s = c.get().getSubCategories();
				category.setSubCategories(s);
			}
		}
		return categoryRepository.save(category);
	}

	@Override
	public Category getCategory(Long id) {
		return categoryRepository.findById(id).get();
	}

	@Override
	public Category getCategoryByName(String name) {
		Optional<Category> category = categoryRepository.findByName(name);
		if(!category.isPresent()) {
			throw new NotFoundException("Category not found");
		}
		return category.get();
	}

	@Override
	public boolean deleteCategory(Category category) {
		categoryRepository.delete(category);
		return true;
	}
	
	@Override
	public List<Category> getCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public SubCategory saveSubCategory(SubCategory subCategory) {
		
		
		Optional<Category> c = categoryRepository.findByName(subCategory.getCategory().getName());
		if(!c.isPresent()) {
			throw new NotFoundException("Category not found");
		}
		subCategory.setCategory(c.get());
		return subCategoryRepository.save(subCategory);
	}

	@Override
	public SubCategory getSubCategory(Long id) {
		return subCategoryRepository.findById(id).get();
	}

	@Override
	public SubCategory getSubCategoryByName(String name) {
		Optional<SubCategory> subCategory = subCategoryRepository.findByName(name);
		if(!subCategory.isPresent()) {
			throw new NotFoundException("SubCategory not found");
		}
		return subCategory.get();
	}

	@Override
	public boolean deleteSubCategory(SubCategory subCategory) {
		subCategoryRepository.delete(subCategory);
		return true;
	}

	@Override
	public List<SubCategory> getSubCategories() {
		return subCategoryRepository.findAll();
	}

}
