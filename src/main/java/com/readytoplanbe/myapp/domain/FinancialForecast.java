package com.readytoplanbe.myapp.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A FinancialForecast.
 */
@Document(collection = "financial_forecast")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinancialForecast implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("start_date")
    private LocalDate startDate;

    @Min(value = 1)
    @Field("duration_in_months")
    private Integer durationInMonths;

    @DBRef
    @Field("manualBusinessPlan")
    private ManualBusinessPlan manualBusinessPlan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public FinancialForecast id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public FinancialForecast startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getDurationInMonths() {
        return this.durationInMonths;
    }

    public FinancialForecast durationInMonths(Integer durationInMonths) {
        this.setDurationInMonths(durationInMonths);
        return this;
    }

    public void setDurationInMonths(Integer durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    public ManualBusinessPlan getManualBusinessPlan() {
        return this.manualBusinessPlan;
    }

    public void setManualBusinessPlan(ManualBusinessPlan manualBusinessPlan) {
        this.manualBusinessPlan = manualBusinessPlan;
    }

    public FinancialForecast manualBusinessPlan(ManualBusinessPlan manualBusinessPlan) {
        this.setManualBusinessPlan(manualBusinessPlan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinancialForecast)) {
            return false;
        }
        return id != null && id.equals(((FinancialForecast) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinancialForecast{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", durationInMonths=" + getDurationInMonths() +
            "}";
    }
}
