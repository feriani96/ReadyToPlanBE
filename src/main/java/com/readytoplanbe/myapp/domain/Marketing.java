package com.readytoplanbe.myapp.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Marketing.
 */
@Document(collection = "marketing")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Marketing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("distribution_channel")
    private String distribution_Channel;

    @Field("marketing_channel")
    private String marketing_channel;

    @Field("sales_target")
    private Long sales_target;

    @Field("currency")
    private String currency;

    @Field("depense")
    private Long depense;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    @DBRef
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getId() {
        return this.id;
    }

    public Marketing id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistribution_Channel() {
        return this.distribution_Channel;
    }

    public Marketing distribution_Channel(String distribution_Channel) {
        this.setDistribution_Channel(distribution_Channel);
        return this;
    }

    public void setDistribution_Channel(String distribution_Channel) {
        this.distribution_Channel = distribution_Channel;
    }

    public String getMarketing_channel() {
        return this.marketing_channel;
    }

    public Marketing marketing_channel(String marketing_channel) {
        this.setMarketing_channel(marketing_channel);
        return this;
    }

    public void setMarketing_channel(String marketing_channel) {
        this.marketing_channel = marketing_channel;
    }

    public Long getSales_target() {
        return this.sales_target;
    }

    public Marketing sales_target(Long sales_target) {
        this.setSales_target(sales_target);
        return this;
    }

    public void setSales_target(Long sales_target) {
        this.sales_target = sales_target;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Marketing currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getDepense() {
        return this.depense;
    }

    public Marketing depense(Long depense) {
        this.setDepense(depense);
        return this;
    }

    public void setDepense(Long depense) {
        this.depense = depense;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Marketing)) {
            return false;
        }
        return id != null && id.equals(((Marketing) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Marketing{" +
            "id=" + getId() +
            ", distribution_Channel='" + getDistribution_Channel() + "'" +
            ", marketing_channel='" + getMarketing_channel() + "'" +
            ", sales_target=" + getSales_target() +
            ", currency='" + getCurrency() + "'" +
            ", depense=" + getDepense() +
            "}";
    }
}
