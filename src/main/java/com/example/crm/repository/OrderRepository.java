package com.example.crm.repository;

import com.example.crm.entity.Orders;
import com.example.crm.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Orders, Integer> {
    List<Orders> findAllByProduct(Product product);
}
