package com.readytoplanbe.myapp.service.dto;

import com.readytoplanbe.myapp.domain.enumeration.Month;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.readytoplanbe.myapp.domain.RevenueForecast} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RevenueForecastDTO implements Serializable {

    private String id;

    @NotNull
    private Month month;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2100)
    private Integer year;

    @NotNull
    @DecimalMin(value = "0")
    private Double unitsSold;

    @NotNull
    @DecimalMin(value = "0")
    private Double totalRevenue;

    private ProductOrServiceDTO productOrService;

    private FinancialForecastDTO financialForecast;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(Double unitsSold) {
        this.unitsSold = unitsSold;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public ProductOrServiceDTO getProductOrService() {
        return productOrService;
    }

    public void setProductOrService(ProductOrServiceDTO productOrService) {
        this.productOrService = productOrService;
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
        if (!(o instanceof RevenueForecastDTO)) {
            return false;
        }

        RevenueForecastDTO revenueForecastDTO = (RevenueForecastDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, revenueForecastDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RevenueForecastDTO{" +
            "id='" + getId() + "'" +
            ", month='" + getMonth() + "'" +
            ", year=" + getYear() +
            ", unitsSold=" + getUnitsSold() +
            ", totalRevenue=" + getTotalRevenue() +
            ", productOrService=" + getProductOrService() +
            ", financialForecast=" + getFinancialForecast() +
            "}";
    }
}
