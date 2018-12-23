package com.example.crm.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String name;

    //  重写equals， 为了能正确使用集合的contains方法
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Customer))
            return false;
        return ((Customer) obj).getId().equals(this.id);
    }

    //  统一哈希码， 为了使用Set的contains方法
    @Override
    public int hashCode() {
        return 3154;
    }

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private List<LinkMan> linkMen;

//    @ManyToMany(fetch = FetchType.LAZY)
//    private List<Employee> employees = new ArrayList<>();

//    @OneToMany
//    @JoinColumn(name = "managed_employee_id")
//    private List<Employee> employees;

    @Column(nullable = false)
    private String tel;

    @Column(nullable = false)
    private String address;

    private String text;

    @Column(nullable = false)
    private Integer credit;

    public Customer() {
    }

    public Customer(String name, String tel, String address, String text, Integer credit) {
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.text = text;
        this.credit = credit;
    }

    public Customer(String name, List<LinkMan> linkMen, String tel, String address, String text, Integer credit) {
        this.name = name;
        this.linkMen = linkMen;
        this.tel = tel;
        this.address = address;
        this.text = text;
        this.credit = credit;
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

    public List<LinkMan> getLinkMen() {
        return linkMen;
    }

    public void setLinkMen(List<LinkMan> linkMen) {
        this.linkMen = linkMen;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }
}
