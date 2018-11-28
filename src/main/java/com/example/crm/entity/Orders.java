package com.example.crm.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private float value;

    @Column(nullable = false)
    private float paidValue;

    @Column(nullable = false)
    private String variety;

    @Column(nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private Boolean billStatus;

    @Column(nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private String aftersaleRecord;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Boolean getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Boolean billStatus) {
        this.billStatus = billStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getAftersaleRecord() {
        return aftersaleRecord;
    }

    public void setAftersaleRecord(String aftersaleRecord) {
        this.aftersaleRecord = aftersaleRecord;
    }
}
