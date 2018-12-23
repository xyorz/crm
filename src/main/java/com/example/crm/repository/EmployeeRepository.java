package com.example.crm.repository;

import com.example.crm.entity.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    Employee findByIdAndPassword(Integer id, String password);
}
