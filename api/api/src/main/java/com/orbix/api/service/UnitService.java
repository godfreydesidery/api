/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import com.orbix.api.domain.Department;
import com.orbix.api.domain.LevelFour;
import com.orbix.api.domain.LevelOne;
import com.orbix.api.domain.LevelThree;
import com.orbix.api.domain.LevelTwo;
import com.orbix.api.domain.SubCategory;
import com.orbix.api.domain.SubClass;
import com.orbix.api.domain.Category;
import com.orbix.api.domain.Class;

/**
 * @author GODFREY
 *
 */
public interface UnitService {
	Department saveDepartment(Department department);
	Department getDepartment(Long id);
	Department getDepartmentByName(String name);
	boolean deleteDepartment(Department department);
	List<Department>getDepartments();
	
	Class saveClass(Class class_);
	Class getClass(Long id);
	Class getClassByName(String name);
	boolean deleteClass(Class class_);
	List<Class>getClasses();
	
	SubClass saveSubClass(SubClass subClass);
	SubClass getSubClass(Long id);
	SubClass getSubClassByName(String name);
	boolean deleteSubClass(SubClass subClass);
	List<SubClass>getSubClasses();
	
	Category saveCategory(Category category);
	Category getCategory(Long id);
	Category getCategoryByName(String name);
	boolean deleteCategory(Category category);
	List<Category>getCategories();
	
	SubCategory saveSubCategory(SubCategory subCategory);
	SubCategory getSubCategory(Long id);
	SubCategory getSubCategoryByName(String name);
	boolean deleteSubCategory(SubCategory subCategory);
	List<SubCategory>getSubCategories();
	
	LevelOne saveLevelOne(LevelOne levelOne);
	LevelOne getLevelOne(Long id);
	LevelOne getLevelOneByName(String name);
	boolean deleteLevelOne(LevelOne levelOne);
	List<LevelOne>getLevelOnes();
	
	LevelTwo saveLevelTwo(LevelTwo levelTwo);
	LevelTwo getLevelTwo(Long id);
	LevelTwo getLevelTwoByName(String name);
	boolean deleteLevelTwo(LevelTwo levelTwo);
	List<LevelTwo>getLevelTwos();
	
	LevelThree saveLevelThree(LevelThree levelThree);
	LevelThree getLevelThree(Long id);
	LevelThree getLevelThreeByName(String name);
	boolean deleteLevelThree(LevelThree levelThree);
	List<LevelThree>getLevelThrees();
	
	LevelFour saveLevelFour(LevelFour levelFour);
	LevelFour getLevelFour(Long id);
	LevelFour getLevelFourByName(String name);
	boolean deleteLevelFour(LevelFour levelFour);
	List<LevelFour>getLevelFours();
}
