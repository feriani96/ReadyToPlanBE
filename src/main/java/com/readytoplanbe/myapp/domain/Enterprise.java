package com.readytoplanbe.myapp.domain;

import com.readytoplanbe.myapp.domain.enumeration.Country;
import com.readytoplanbe.myapp.domain.enumeration.Currency;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Enterprise.
 */
@Document(collection = "enterprise")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enterprise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(max = 50)
    @Field("enterprise_name")
    private String enterpriseName;

    @NotNull
    @Field("country")
    private Country country;

    @NotNull
    @Field("phone_number")
    private Integer phoneNumber;

    @NotNull
    @Size(max = 1000)
    @Field("description")
    private String description;

    @NotNull
    @Field("amount")
    private Long amount;

    @NotNull
    @Field("currency")
    private Currency currency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Enterprise id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnterpriseName() {
        return this.enterpriseName;
    }

    public Enterprise enterpriseName(String enterpriseName) {
        this.setEnterpriseName(enterpriseName);
        return this;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Country getCountry() {
        return this.country;
    }

    public Enterprise country(Country country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Integer getPhoneNumber() {
        return this.phoneNumber;
    }

    public Enterprise phoneNumber(Integer phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public Enterprise description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAmount() {
        return this.amount;
    }

    public Enterprise amount(Long amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public Enterprise currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enterprise)) {
            return false;
        }
        return id != null && id.equals(((Enterprise) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enterprise{" +
            "id=" + getId() +
            ", enterpriseName='" + getEnterpriseName() + "'" +
            ", country='" + getCountry() + "'" +
            ", phoneNumber=" + getPhoneNumber() +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            "}";
    }
}
