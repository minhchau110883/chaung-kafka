package com.chaung.kafka.service.dto;


import java.io.Serializable;
import java.util.Objects;
import com.chaung.kafka.domain.enumeration.UserType;

/**
 * A DTO for the Campaign entity.
 */
public class CampaignDTO implements Serializable {

    private Long id;

    private String name;

    private Double minAmount;

    private Double maxAmount;

    private Integer point;

    private UserType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CampaignDTO campaignDTO = (CampaignDTO) o;
        if(campaignDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), campaignDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CampaignDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", minAmount=" + getMinAmount() +
            ", maxAmount=" + getMaxAmount() +
            ", point=" + getPoint() +
            ", type='" + getType() + "'" +
            "}";
    }
}
