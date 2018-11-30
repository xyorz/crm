package com.example.crm.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class SaleOpportunity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private Boolean isDeclare;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Employee employee;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "saleOpportunityId")
    private List<Product> products;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Customer customer;

    @Column(nullable = false)
    private String record;

    public SaleOpportunity() {
    }

    public SaleOpportunity(Boolean isDeclare, Employee employee, List<Product> productList, Customer customer, String record) {
        this.isDeclare = isDeclare;
        this.employee = employee;
        this.products = productList;
        this.customer = customer;
        this.record = record;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDeclare() {
        return isDeclare;
    }

    public void setDeclare(Boolean declare) {
        isDeclare = declare;
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

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
