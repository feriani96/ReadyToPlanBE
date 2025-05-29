package com.readytoplanbe.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.readytoplanbe.myapp.domain.ExpenseForecast} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExpenseForecastDTO implements Serializable {

    private String id;

    @NotNull
    @Size(max = 100)
    private String label;

    @NotNull
    @DecimalMin(value = "0")
    private Double monthlyAmount;

    private FinancialForecastDTO financialForecast;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getMonthlyAmount() {
        return monthlyAmount;
    }

    public void setMonthlyAmount(Double monthlyAmount) {
        this.monthlyAmount = monthlyAmount;
    }

    public FinancialForecastDTO getFinancialForecast() {
        return financialForecast;
    }

    public void setFinancialForecast(FinancialForecastDTO financialForecast) {
        this.financialForecast = financialForecast;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExpenseForecastDTO)) {
            return false;
        }

        ExpenseForecastDTO expenseForecastDTO = (ExpenseForecastDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, expenseForecastDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpenseForecastDTO{" +
            "id='" + getId() + "'" +
            ", label='" + getLabel() + "'" +
            ", monthlyAmount=" + getMonthlyAmount() +
            ", financialForecast=" + getFinancialForecast() +
            "}";
    }
}
