package com.example.crm.repository;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    public Employee findByIdAndPassword(Integer id, String password);
    @Query("select e.customers from Employee e where e.id = ?1")
    public List<Customer> queryById(Integer id);
}
