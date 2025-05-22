package com.readytoplanbe.myapp.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ProductOrService.
 */
@Document(collection = "product_or_service")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductOrService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(max = 50)
    @Field("name_product_or_service")
    private String nameProductOrService;

    @NotNull
    @Size(max = 50)
    @Field("product_description")
    private String productDescription;

    @NotNull
    @DecimalMin(value = "0")
    @Field("unit_price")
    private Double unitPrice;

    @Field("estimated_monthly_sales")
    private Integer estimatedMonthlySales;

    @NotNull
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

    public ProductOrService id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameProductOrService() {
        return this.nameProductOrService;
    }

    public ProductOrService nameProductOrService(String nameProductOrService) {
        this.setNameProductOrService(nameProductOrService);
        return this;
    }

    public void setNameProductOrService(String nameProductOrService) {
        this.nameProductOrService = nameProductOrService;
    }

    public String getProductDescription() {
        return this.productDescription;
    }

    public ProductOrService productDescription(String productDescription) {
        this.setProductDescription(productDescription);
        return this;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public ProductOrService unitPrice(Double unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getEstimatedMonthlySales() {
        return this.estimatedMonthlySales;
    }

    public ProductOrService estimatedMonthlySales(Integer estimatedMonthlySales) {
        this.setEstimatedMonthlySales(estimatedMonthlySales);
        return this;
    }

    public void setEstimatedMonthlySales(Integer estimatedMonthlySales) {
        this.estimatedMonthlySales = estimatedMonthlySales;
    }

    public Integer getDurationInMonths() {
        return this.durationInMonths;
    }

    public ProductOrService durationInMonths(Integer durationInMonths) {
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

    public ProductOrService manualBusinessPlan(ManualBusinessPlan manualBusinessPlan) {
        this.setManualBusinessPlan(manualBusinessPlan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOrService)) {
            return false;
        }
        return id != null && id.equals(((ProductOrService) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOrService{" +
            "id=" + getId() +
            ", nameProductOrService='" + getNameProductOrService() + "'" +
            ", productDescription='" + getProductDescription() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", estimatedMonthlySales=" + getEstimatedMonthlySales() +
            ", durationInMonths=" + getDurationInMonths() +
            "}";
    }
}
