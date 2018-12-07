package com.example.crm.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class SaleOpportunity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn
    private Employee employee;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinColumn(name = "saleOpportunityId")
    private List<Product> products;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn
    private Customer customer;

    public SaleOpportunity() {
    }

    public SaleOpportunity(Employee employee, List<Product> productList, Customer customer) {
        this.employee = employee;
        this.products = productList;
        this.customer = customer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }



}
