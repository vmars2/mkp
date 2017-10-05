package com.shravya.mkp;

import com.yahoo.elide.annotation.Exclude;
import com.yahoo.elide.annotation.SharePermission;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.Period;

@Entity
@SharePermission(expression = "Prefab.Role.All")
public class Bid {

    private long id;
    private BigDecimal amount;
    private Period duration;
    private RateType rateType;
    private Status status;
    private Project project;
    private Seller seller;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Period getDuration() {
        return duration;
    }

    public void setDuration(Period duration) {
        this.duration = duration;
    }

    public RateType getRateType() {
        return rateType;
    }

    public void setRateType(RateType rateType) {
        this.rateType = rateType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Project getProject() {
        return project;
    }

    @ManyToOne
    public void setProject(Project project) {
        this.project = project;
    }

    @ManyToOne
    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public enum RateType {
        HOURLY,
        CUMULATIVE
    }

    public enum Status {
        OPEN,
        CLOSED,
        SUCCESS
    }
}
