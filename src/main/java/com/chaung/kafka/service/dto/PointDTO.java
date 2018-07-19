package com.chaung.kafka.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Point entity.
 */
public class PointDTO implements Serializable {

    private Long id;

    private Long outletId;

    private Long terminalId;

    private Long merchantId;

    private Long userId;

    private Long campaignId;

    private Long paymentTransaction;

    private String paymentType;

    private Double amount;

    private Integer point;

    private Long campaignId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getPaymentTransaction() {
        return paymentTransaction;
    }

    public void setPaymentTransaction(Long paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PointDTO pointDTO = (PointDTO) o;
        if(pointDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pointDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PointDTO{" +
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
