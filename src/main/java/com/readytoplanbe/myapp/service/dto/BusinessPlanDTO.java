package com.readytoplanbe.myapp.service.dto;

import com.readytoplanbe.myapp.domain.enumeration.Country;
import com.readytoplanbe.myapp.domain.enumeration.Currency;
import com.readytoplanbe.myapp.domain.enumeration.Languages;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.readytoplanbe.myapp.domain.BusinessPlan} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessPlanDTO implements Serializable {

    private String id;

    @NotNull
    @Size(max = 50)
    private String companyName;

    @NotNull
    private LocalDate companyStartDate;

    @NotNull
    private Country country;

    @NotNull
    private Languages languages;

    @NotNull
    @Size(max = 1000)
    private String companyDescription;

    @NotNull
    private Double anticipatedProjectSize;

    @NotNull
    private Currency currency;

    private String generatedPresentation;

    private String presentationContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getCompanyStartDate() {
        return companyStartDate;
    }

    public void setCompanyStartDate(LocalDate companyStartDate) {
        this.companyStartDate = companyStartDate;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Languages getLanguages() {
        return languages;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public Double getAnticipatedProjectSize() {
        return anticipatedProjectSize;
    }

    public void setAnticipatedProjectSize(Double anticipatedProjectSize) {
        this.anticipatedProjectSize = anticipatedProjectSize;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getGeneratedPresentation() {
        return generatedPresentation;
    }

    public void setGeneratedPresentation(String generatedPresentation) {
        this.generatedPresentation = generatedPresentation;
    }

    public String getPresentationContent() { return presentationContent; }

    public void setPresentationContent(String presentationContent) {
        this.presentationContent = presentationContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessPlanDTO)) {
            return false;
        }

        BusinessPlanDTO businessPlanDTO = (BusinessPlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, businessPlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessPlanDTO{" +
            "id='" + getId() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", companyStartDate='" + getCompanyStartDate() + "'" +
            ", country='" + getCountry() + "'" +
            ", languages='" + getLanguages() + "'" +
            ", companyDescription='" + getCompanyDescription() + "'" +
            ", anticipatedProjectSize=" + getAnticipatedProjectSize() +
            ", currency='" + getCurrency() + "'" +
            ", generatedPresentation='" + getGeneratedPresentation() + "'" +
            "}";
    }
}
