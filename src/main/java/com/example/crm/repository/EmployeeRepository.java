package com.example.crm.repository;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
<<<<<<< HEAD
    Employee findByIdAndPassword(Integer id, String password);
=======
    public Employee findByIdAndPassword(Integer id, String password);
    @Query("select e.customers from Employee e where e.id = ?1")
    public List<Customer> queryById(Integer id);
>>>>>>> 9fc40899b01d3a8a60fd244c5d2691420de05bd5
}
