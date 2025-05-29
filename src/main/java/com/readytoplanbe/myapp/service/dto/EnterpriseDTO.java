package com.readytoplanbe.myapp.service.dto;

import com.readytoplanbe.myapp.domain.enumeration.Country;
import com.readytoplanbe.myapp.domain.enumeration.Currency;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.readytoplanbe.myapp.domain.Enterprise} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnterpriseDTO implements Serializable {

    private String id;

    @NotNull
    @Size(max = 50)
    private String enterpriseName;

    @NotNull
    private Country country;

    @NotNull
    private Integer phoneNumber;

    @NotNull
    @Size(max = 1000)
    private String description;

    @NotNull
    private Long amount;

    @NotNull
    private Currency currency;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnterpriseDTO)) {
            return false;
        }

        EnterpriseDTO enterpriseDTO = (EnterpriseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enterpriseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnterpriseDTO{" +
            "id='" + getId() + "'" +
            ", enterpriseName='" + getEnterpriseName() + "'" +
            ", country='" + getCountry() + "'" +
            ", phoneNumber=" + getPhoneNumber() +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            "}";
    }
}
