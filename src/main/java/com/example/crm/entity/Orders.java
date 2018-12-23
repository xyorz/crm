package com.example.crm.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn
    private Customer customer;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn
    private Employee employee;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn
    private Product product;

    @Column(nullable = false)
    private boolean status;

    @Column(nullable = false)
    private boolean receiptStatus;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private float value;

    @Column(nullable = false)
    private float paidValue;

    @Column(nullable = false)
    private String record;

    public Orders() {
    }

    public Orders(Customer customer, Employee employee, Product product, boolean status, boolean receiptStatus, Date date, float value, float paidValue, String record) {
        this.customer = customer;
        this.employee = employee;
        this.product = product;
        this.status = status;
        this.receiptStatus = receiptStatus;
        this.date = date;
        this.value = value;
        this.paidValue = paidValue;
        this.record = record;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(boolean receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getPaidValue() {
        return paidValue;
    }

    public void setPaidValue(float paidValue) {
        this.paidValue = paidValue;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
