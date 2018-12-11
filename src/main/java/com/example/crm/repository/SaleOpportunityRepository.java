package com.example.crm.repository;

import com.example.crm.entity.Employee;
import com.example.crm.entity.SaleOpportunity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SaleOpportunityRepository extends CrudRepository<SaleOpportunity, Integer> {
    List<SaleOpportunity> findAllByEmployeeAndIsDeclareFalse(Employee employee);
    List<SaleOpportunity> findAllByFindEmployee(Employee findEmployee);
}
