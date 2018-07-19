package com.chaung.kafka.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Point.
 */
@Entity
@Table(name = "point")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Point implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "outlet_id")
    private Long outletId;

    @Column(name = "terminal_id")
    private Long terminalId;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "campaign_id")
    private Long campaignId;

    @Column(name = "payment_transaction")
    private Long paymentTransaction;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "point")
    private Integer point;

    @ManyToOne
    private Campaign campaign;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOutletId() {
        return outletId;
    }

    public Point outletId(Long outletId) {
        this.outletId = outletId;
        return this;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public Long getTerminalId() {
        return terminalId;
    }

    public Point terminalId(Long terminalId) {
        this.terminalId = terminalId;
        return this;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public Point merchantId(Long merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getUserId() {
        return userId;
    }

    public Point userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public Point campaignId(Long campaignId) {
        this.campaignId = campaignId;
        return this;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getPaymentTransaction() {
        return paymentTransaction;
    }

    public Point paymentTransaction(Long paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
        return this;
    }

    public void setPaymentTransaction(Long paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public Point paymentType(String paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getAmount() {
        return amount;
    }

    public Point amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPoint() {
        return point;
    }

    public Point point(Integer point) {
        this.point = point;
        return this;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public Point campaign(Campaign campaign) {
        this.campaign = campaign;
        return this;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        if (point.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), point.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Point{" +
            "id=" + getId() +
            ", outletId=" + getOutletId() +
            ", terminalId=" + getTerminalId() +
            ", merchantId=" + getMerchantId() +
            ", userId=" + getUserId() +
            ", campaignId=" + getCampaignId() +
            ", paymentTransaction=" + getPaymentTransaction() +
            ", paymentType='" + getPaymentType() + "'" +
            ", amount=" + getAmount() +
            ", point=" + getPoint() +
            "}";
    }
}
