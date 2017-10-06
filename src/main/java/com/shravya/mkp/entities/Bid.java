package com.shravya.mkp.entities;

import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.OnCreatePreSecurity;
import com.yahoo.elide.annotation.OnUpdatePreSecurity;
import com.yahoo.elide.annotation.SharePermission;
import io.dropwizard.validation.ValidationMethod;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Entity
@Table(name = "bid")
@SharePermission(expression = "Prefab.Role.All")
@Include(rootLevel = true)
public class Bid {

    private long id;
    private BigDecimal totalQuote;  // The totalQuote is in USD
    private BigDecimal hourlyQuote; // the hourlyQuote is in USD
    private int noOfHrs;           // noOfHrs is in hrs
    private Status status = Status.OPEN;
    private Project project;
    private Buyer buyer;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getTotalQuote() {
        return totalQuote;
    }

    public void setTotalQuote(BigDecimal totalQuote) {
        this.totalQuote = totalQuote;
    }

    public BigDecimal getHourlyQuote() {
        return hourlyQuote;
    }

    public void setHourlyQuote(BigDecimal hourlyQuote) {
        this.hourlyQuote = hourlyQuote;
    }

    public int getNoOfHrs() {
        return noOfHrs;
    }

    public void setNoOfHrs(int noOfHrs) {
        this.noOfHrs = noOfHrs;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @ManyToOne
    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    @ValidationMethod(message="The quote setting is not valid")
    @Transient
    public boolean isQuoteValid() {
        return (totalQuote != null || (hourlyQuote != null && noOfHrs > 0));
    }

    @OnCreatePreSecurity
    public void onCreatePreSecurity() {
        updateFinalTotalQuote();
    }

    @OnUpdatePreSecurity
    public void onUpdatePreCommit() {
        updateFinalTotalQuote();
    }

    public void updateFinalTotalQuote() {
        if(this.hourlyQuote != null) {
            this.totalQuote =  hourlyQuote.multiply(new BigDecimal(noOfHrs));
        }
    }

    public enum Status {
        OPEN,
        ACCEPTED,
        REJECTED
    }
}
