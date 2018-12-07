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

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn
    private Employee employee;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn
    private Employee findEmployee;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinColumn(name = "saleOpportunityId")
    private List<Product> products;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn
    private Customer customer;

    public SaleOpportunity() {
    }

    public SaleOpportunity(Boolean isDeclare, Employee employee, Employee findEmployee, List<Product> products, Customer customer) {
        this.isDeclare = isDeclare;
        this.employee = employee;
        this.findEmployee = findEmployee;
        this.products = products;
        this.customer = customer;
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

    public Employee getFindEmployee() {
        return findEmployee;
    }

    public void setFindEmployee(Employee findEmployee) {
        this.findEmployee = findEmployee;
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
