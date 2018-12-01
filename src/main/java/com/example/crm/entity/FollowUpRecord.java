package com.example.crm.entity;
import javax.persistence.*;

@Entity
public class FollowUpRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "employeeId", referencedColumnName = "id")
    private Employee employee;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "customerId", referencedColumnName = "id")
    private Customer customer;

    private String record;

    @Column(nullable = false)
    private Boolean isDeclare;

    public FollowUpRecord(){}

    public FollowUpRecord(Employee employee, Customer customer, String record, Boolean isDeclare) {
        this.employee = employee;
        this.customer = customer;
        this.record = record;
        this.isDeclare = isDeclare;
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
}
