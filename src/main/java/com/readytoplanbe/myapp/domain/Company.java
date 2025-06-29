package com.readytoplanbe.myapp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Company.
 */
@Document(collection = "company")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("enterprise_name")
    private String enterpriseName;

    @Field("phone_number")
    private Long phoneNumber;

    @Field("description")
    private String description;

    @Field("amount")
    private BigDecimal amount;

    @Field("country")
    private String country;

    @Field("currency")
    private String currency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Company id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnterpriseName() {
        return this.enterpriseName;
    }

    public Company enterpriseName(String enterpriseName) {
        this.setEnterpriseName(enterpriseName);
        return this;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Long getPhoneNumber() {
        return this.phoneNumber;
    }

    public Company phoneNumber(Long phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public Company description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Company amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCountry() {
        return this.country;
    }

    public Company country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Company currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", enterpriseName='" + getEnterpriseName() + "'" +
            ", phoneNumber=" + getPhoneNumber() +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", country='" + getCountry() + "'" +
            ", currency='" + getCurrency() + "'" +
            "}";
    }
}
