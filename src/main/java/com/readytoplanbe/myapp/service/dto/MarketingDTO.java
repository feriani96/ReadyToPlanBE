package com.readytoplanbe.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.readytoplanbe.myapp.domain.Marketing} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MarketingDTO implements Serializable {

    private String id;

    private String distribution_Channel;

    private String marketing_channel;

    private Long sales_target;

    private String currency;

    private Long depense;
    private String companyId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistribution_Channel() {
        return distribution_Channel;
    }

    public void setDistribution_Channel(String distribution_Channel) {
        this.distribution_Channel = distribution_Channel;
    }

    public String getMarketing_channel() {
        return marketing_channel;
    }

    public void setMarketing_channel(String marketing_channel) {
        this.marketing_channel = marketing_channel;
    }

    public Long getSales_target() {
        return sales_target;
    }

    public void setSales_target(Long sales_target) {
        this.sales_target = sales_target;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getDepense() {
        return depense;
    }

    public void setDepense(Long depense) {
        this.depense = depense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarketingDTO)) {
            return false;
        }

        MarketingDTO marketingDTO = (MarketingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, marketingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarketingDTO{" +
            "id='" + getId() + "'" +
            ", distribution_Channel='" + getDistribution_Channel() + "'" +
            ", marketing_channel='" + getMarketing_channel() + "'" +
            ", sales_target=" + getSales_target() +
            ", currency='" + getCurrency() + "'" +
            ", depense=" + getDepense() +
            "}";
    }
}
