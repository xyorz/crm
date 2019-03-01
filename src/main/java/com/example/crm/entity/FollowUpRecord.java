package com.example.crm.entity;
import javax.persistence.*;
import java.util.Date;

@Entity
public class FollowUpRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "saleOpportunityId", referencedColumnName = "id")
    private SaleOpportunity saleOpportunity;

    @Column
    private String record;

    @Column
    private Date date;

    @Column(nullable = false)
    private Boolean isDeclare;

    @Column
    private float cost;

    public FollowUpRecord(){}

    public FollowUpRecord(SaleOpportunity saleOpportunity, float cost,String record, Date date, Boolean isDeclare) {
        this.cost = cost;
        this.saleOpportunity = saleOpportunity;
        this.record = record;
        this.date = date;
        this.isDeclare = isDeclare;
    }

    public SaleOpportunity getSaleOpportunity() {
        return saleOpportunity;
    }

    public void setSaleOpportunity(SaleOpportunity saleOpportunity) {
        this.saleOpportunity = saleOpportunity;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
