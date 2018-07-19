package com.chaung.kafka.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Point entity. This class is used in PointResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /points?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PointCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter outletId;

    private LongFilter terminalId;

    private LongFilter merchantId;

    private LongFilter userId;

    private LongFilter campaignId;

    private LongFilter paymentTransaction;

    private StringFilter paymentType;

    private DoubleFilter amount;

    private IntegerFilter point;

    private LongFilter campaignId;

    public PointCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getOutletId() {
        return outletId;
    }

    public void setOutletId(LongFilter outletId) {
        this.outletId = outletId;
    }

    public LongFilter getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(LongFilter terminalId) {
        this.terminalId = terminalId;
    }

    public LongFilter getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(LongFilter merchantId) {
        this.merchantId = merchantId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(LongFilter campaignId) {
        this.campaignId = campaignId;
    }

    public LongFilter getPaymentTransaction() {
        return paymentTransaction;
    }

    public void setPaymentTransaction(LongFilter paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
    }

    public StringFilter getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(StringFilter paymentType) {
        this.paymentType = paymentType;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public IntegerFilter getPoint() {
        return point;
    }

    public void setPoint(IntegerFilter point) {
        this.point = point;
    }

    public LongFilter getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(LongFilter campaignId) {
        this.campaignId = campaignId;
    }

    @Override
    public String toString() {
        return "PointCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (outletId != null ? "outletId=" + outletId + ", " : "") +
                (terminalId != null ? "terminalId=" + terminalId + ", " : "") +
                (merchantId != null ? "merchantId=" + merchantId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (campaignId != null ? "campaignId=" + campaignId + ", " : "") +
                (paymentTransaction != null ? "paymentTransaction=" + paymentTransaction + ", " : "") +
                (paymentType != null ? "paymentType=" + paymentType + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (point != null ? "point=" + point + ", " : "") +
                (campaignId != null ? "campaignId=" + campaignId + ", " : "") +
            "}";
    }

}
