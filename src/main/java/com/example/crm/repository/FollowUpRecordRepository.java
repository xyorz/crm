package com.example.crm.repository;

import com.example.crm.entity.Employee;
import com.example.crm.entity.FollowUpRecord;
import com.example.crm.entity.SaleOpportunity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FollowUpRecordRepository extends CrudRepository<FollowUpRecord, Integer> {
    List<FollowUpRecord> findAllBySaleOpportunity(SaleOpportunity saleOpportunity);
}
