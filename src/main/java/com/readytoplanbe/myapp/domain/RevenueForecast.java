package com.readytoplanbe.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.readytoplanbe.myapp.domain.enumeration.Month;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A RevenueForecast.
 */
@Document(collection = "revenue_forecast")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RevenueForecast implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("month")
    private Month month;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2100)
    @Field("year")
    private Integer year;

    @NotNull
    @DecimalMin(value = "0")
    @Field("units_sold")
    private Double unitsSold;

    @NotNull
    @DecimalMin(value = "0")
    @Field("total_revenue")
    private Double totalRevenue;

    @DBRef
    @Field("productOrService")
    @JsonIgnoreProperties(value = { "manualBusinessPlan" }, allowSetters = true)
    private ProductOrService productOrService;

    @DBRef
    @Field("financialForecast")
    @JsonIgnoreProperties(value = { "manualBusinessPlan" }, allowSetters = true)
    private FinancialForecast financialForecast;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public RevenueForecast id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Month getMonth() {
        return this.month;
    }

    public RevenueForecast month(Month month) {
        this.setMonth(month);
        return this;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return this.year;
    }

    public RevenueForecast year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getUnitsSold() {
        return this.unitsSold;
    }

    public RevenueForecast unitsSold(Double unitsSold) {
        this.setUnitsSold(unitsSold);
        return this;
    }

    public void setUnitsSold(Double unitsSold) {
        this.unitsSold = unitsSold;
    }

    public Double getTotalRevenue() {
        return this.totalRevenue;
    }

    public RevenueForecast totalRevenue(Double totalRevenue) {
        this.setTotalRevenue(totalRevenue);
        return this;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public ProductOrService getProductOrService() {
        return this.productOrService;
    }

    public void setProductOrService(ProductOrService productOrService) {
        this.productOrService = productOrService;
    }

    public RevenueForecast productOrService(ProductOrService productOrService) {
        this.setProductOrService(productOrService);
        return this;
    }

    public FinancialForecast getFinancialForecast() {
        return this.financialForecast;
    }

    public void setFinancialForecast(FinancialForecast financialForecast) {
        this.financialForecast = financialForecast;
    }

    public RevenueForecast financialForecast(FinancialForecast financialForecast) {
        this.setFinancialForecast(financialForecast);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RevenueForecast)) {
            return false;
        }
        return id != null && id.equals(((RevenueForecast) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RevenueForecast{" +
            "id=" + getId() +
            ", month='" + getMonth() + "'" +
            ", year=" + getYear() +
            ", unitsSold=" + getUnitsSold() +
            ", totalRevenue=" + getTotalRevenue() +
            "}";
    }
}
