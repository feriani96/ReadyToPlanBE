package com.readytoplanbe.myapp.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ExpenseForecast.
 */
@Document(collection = "expense_forecast")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExpenseForecast implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(max = 100)
    @Field("label")
    private String label;

    @NotNull
    @DecimalMin(value = "0")
    @Field("monthly_amount")
    private Double monthlyAmount;

    @DBRef
    @Field("financialForecast")
    private FinancialForecast financialForecast;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ExpenseForecast id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public ExpenseForecast label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getMonthlyAmount() {
        return this.monthlyAmount;
    }

    public ExpenseForecast monthlyAmount(Double monthlyAmount) {
        this.setMonthlyAmount(monthlyAmount);
        return this;
    }

    public void setMonthlyAmount(Double monthlyAmount) {
        this.monthlyAmount = monthlyAmount;
    }

    public FinancialForecast getFinancialForecast() {
        return this.financialForecast;
    }

    public void setFinancialForecast(FinancialForecast financialForecast) {
        this.financialForecast = financialForecast;
    }

    public ExpenseForecast financialForecast(FinancialForecast financialForecast) {
        this.setFinancialForecast(financialForecast);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExpenseForecast)) {
            return false;
        }
        return id != null && id.equals(((ExpenseForecast) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpenseForecast{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", monthlyAmount=" + getMonthlyAmount() +
            "}";
    }
}
