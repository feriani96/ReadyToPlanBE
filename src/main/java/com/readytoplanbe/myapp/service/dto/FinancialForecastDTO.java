package com.readytoplanbe.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.readytoplanbe.myapp.domain.FinancialForecast} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinancialForecastDTO implements Serializable {

    private String id;

    @NotNull
    private LocalDate startDate;

    @Min(value = 1)
    private Integer durationInMonths;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(Integer durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinancialForecastDTO)) {
            return false;
        }

        FinancialForecastDTO financialForecastDTO = (FinancialForecastDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, financialForecastDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinancialForecastDTO{" +
            "id='" + getId() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", durationInMonths=" + getDurationInMonths() +
            "}";
    }
}
