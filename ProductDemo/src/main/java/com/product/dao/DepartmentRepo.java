package com.product.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.product.model.Department;

@Repository
public interface DepartmentRepo extends CrudRepository<Department, Integer> {
	@Query("from Department d where d.id > ?1")
	List<Department> getRecentDepartments(int detid);
	
	@Query(value ="select avg(length(department_name)) from department", nativeQuery = true)
	int getAvgLength();

}
