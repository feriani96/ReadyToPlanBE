package com.readytoplanbe.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.readytoplanbe.myapp.domain.ProductOrService} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductOrServiceDTO implements Serializable {

    private String id;

    @NotNull
    @Size(max = 50)
    private String nameProductOrService;

    @NotNull
    @Size(max = 50)
    private String productDescription;

    @NotNull
    @DecimalMin(value = "0")
    private Double unitPrice;

    private Integer estimatedMonthlySales;

    @NotNull
    @Min(value = 1)
    private Integer durationInMonths;

    private ManualBusinessPlanDTO manualBusinessPlan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameProductOrService() {
        return nameProductOrService;
    }

    public void setNameProductOrService(String nameProductOrService) {
        this.nameProductOrService = nameProductOrService;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getEstimatedMonthlySales() {
        return estimatedMonthlySales;
    }

    public void setEstimatedMonthlySales(Integer estimatedMonthlySales) {
        this.estimatedMonthlySales = estimatedMonthlySales;
    }

    public Integer getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(Integer durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    public ManualBusinessPlanDTO getManualBusinessPlan() {
        return manualBusinessPlan;
    }

    public void setManualBusinessPlan(ManualBusinessPlanDTO manualBusinessPlan) {
        this.manualBusinessPlan = manualBusinessPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOrServiceDTO)) {
            return false;
        }

        ProductOrServiceDTO productOrServiceDTO = (ProductOrServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productOrServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOrServiceDTO{" +
            "id='" + getId() + "'" +
            ", nameProductOrService='" + getNameProductOrService() + "'" +
            ", productDescription='" + getProductDescription() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", estimatedMonthlySales=" + getEstimatedMonthlySales() +
            ", durationInMonths=" + getDurationInMonths() +
            ", manualBusinessPlan=" + getManualBusinessPlan() +
            "}";
    }
}
