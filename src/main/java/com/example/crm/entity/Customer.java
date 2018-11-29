package com.example.crm.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private List<LinkMan> linkMen;

    @Column(nullable = false)
    private String tel;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
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
