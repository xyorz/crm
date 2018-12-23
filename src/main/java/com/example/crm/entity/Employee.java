package com.example.crm.entity;


import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean sex;

    @Column(nullable = false)
    private String tel;

//    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
//    @JoinTable(joinColumns = {@JoinColumn(name = "employee_id")},
//            inverseJoinColumns = {@JoinColumn(name = "customer_id")})
//    private List<Customer> customers = new ArrayList<>();

    @JoinTable(joinColumns={@JoinColumn(name="employee_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="customer_id", referencedColumnName="id")})
    @ManyToMany(fetch=FetchType.EAGER)
    private Set<Customer> customers = new HashSet<>();

//    @ManyToMany
//    @JoinColumn(name = "manage_customer_id")
//    private List<Customer> customers;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean empty;

    @Column(nullable = false)
    private String remark;

    @Column(nullable = false)
    private Integer level;

    public Employee() {
    }

    public Employee(String name, Boolean sex, String tel, String password, Boolean empty, String remark, Integer level) {
        this.name = name;
        this.sex = sex;
        this.tel = tel;
        this.password = password;
        this.empty = empty;
        this.remark = remark;
        this.level = level;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
