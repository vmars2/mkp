package com.shravya.mkp.entities;

import com.yahoo.elide.annotation.ComputedAttribute;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.OnCreatePreCommit;
import com.yahoo.elide.annotation.OnUpdatePreCommit;
import com.yahoo.elide.annotation.SharePermission;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;

/**
 * Created by syakkali on 05/10/17.
 */
@Entity
@Table(name = "project")
@SharePermission(expression = "Prefab.Role.All")
@Include(rootLevel = true)
public class Project {

    private long id;
    private String name;
    private String description;
    private long deadline;          // Time is seconds from epoch
    private Status status = Status.OPEN;
    private Seller seller;
    private Collection<Bid> bids;
    private long dateOfCreation;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDeadline() {
        return deadline;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne
    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    public Collection<Bid> getBids() {
        return bids;
    }

    public void setBids(Collection<Bid> bids) {
        this.bids = bids;
    }

    @ComputedAttribute
    @Transient
    @OneToOne
    public Bid getBestBid() {
        return evaluateBestBid();
    }

    public void setBestBid(Long bestBid) { }

    @GeneratedValue
    public long getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(long dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    @OnUpdatePreCommit
    public void onUpdatePreCommit() {
        validateDeadline();
    }

    @OnCreatePreCommit
    public void onCreatePreCommit() {
        validateDeadline();
    }

    private Bid evaluateBestBid() {
        Bid bestBid = null;
        BigDecimal min = BigDecimal.valueOf(Integer.MAX_VALUE);
        for (Bid bid: getBids()) {
            if (bid.getTotalQuote().compareTo(min) < 0) {
                min = bid.getTotalQuote();
                bestBid = bid;
            } else if (bid.getTotalQuote().equals(min)) {
                if (bid.getDateOfCreation() < bestBid.getDateOfCreation()) {
                    bestBid = bid;
                }
            }
        }
        return bestBid;
    }

    private void validateDeadline() {

        int numOfDigits = String.valueOf(this.deadline).length();
        if(numOfDigits > 10) {
            throw new IllegalArgumentException("deadline has to be in seconds from epoch");
        }

        long now = Instant.now().getEpochSecond();
        if (this.deadline <= now) {
            throw new IllegalArgumentException("deadline cannot be in the past");
        }
    }

    /**
     * The various possible Project status
     */
    public enum Status {
        OPEN,
        CLOSED
    }
}
