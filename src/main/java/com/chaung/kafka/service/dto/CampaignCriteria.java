package com.chaung.kafka.service.dto;

import java.io.Serializable;
import com.chaung.kafka.domain.enumeration.UserType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Campaign entity. This class is used in CampaignResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /campaigns?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CampaignCriteria implements Serializable {
    /**
     * Class for filtering UserType
     */
    public static class UserTypeFilter extends Filter<UserType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private DoubleFilter minAmount;

    private DoubleFilter maxAmount;

    private IntegerFilter point;

    private UserTypeFilter type;

    public CampaignCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public DoubleFilter getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(DoubleFilter minAmount) {
        this.minAmount = minAmount;
    }

    public DoubleFilter getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(DoubleFilter maxAmount) {
        this.maxAmount = maxAmount;
    }

    public IntegerFilter getPoint() {
        return point;
    }

    public void setPoint(IntegerFilter point) {
        this.point = point;
    }

    public UserTypeFilter getType() {
        return type;
    }

    public void setType(UserTypeFilter type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CampaignCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (minAmount != null ? "minAmount=" + minAmount + ", " : "") +
                (maxAmount != null ? "maxAmount=" + maxAmount + ", " : "") +
                (point != null ? "point=" + point + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
            "}";
    }

}
