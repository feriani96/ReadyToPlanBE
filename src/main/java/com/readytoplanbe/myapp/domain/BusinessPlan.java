package com.readytoplanbe.myapp.domain;

import com.readytoplanbe.myapp.domain.enumeration.Country;
import com.readytoplanbe.myapp.domain.enumeration.Currency;
import com.readytoplanbe.myapp.domain.enumeration.Languages;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A BusinessPlan.
 */
@Document(collection = "business_plan")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(max = 50)
    @Field("company_name")
    private String companyName;

    @NotNull
    @Field("company_start_date")
    private LocalDate companyStartDate;

    @NotNull
    @Field("country")
    private Country country;

    @NotNull
    @Field("languages")
    private Languages languages;

    @NotNull
    @Size(max = 1000)
    @Field("company_description")
    private String companyDescription;

    @NotNull
    @Field("anticipated_project_size")
    private Double anticipatedProjectSize;

    @NotNull
    @Field("currency")
    private Currency currency;

    @Field("generated_presentation")
    private String generatedPresentation;

    // Nouveau champ pour stocker la présentation éditée manuellement
    @Field("edited_presentation")
    private String editedPresentation;

    // Flag pour indiquer si la présentation a été modifiée manuellement
    @Field("is_edited")
    private boolean isEdited = false;

    @Field("presentation_content")
    private String presentationContent;

    @Field("cover_image_url")
    private String coverImageUrl;


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public BusinessPlan id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public BusinessPlan companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getCompanyStartDate() {
        return this.companyStartDate;
    }

    public BusinessPlan companyStartDate(LocalDate companyStartDate) {
        this.setCompanyStartDate(companyStartDate);
        return this;
    }

    public void setCompanyStartDate(LocalDate companyStartDate) {
        this.companyStartDate = companyStartDate;
    }

    public Country getCountry() {
        return this.country;
    }

    public BusinessPlan country(Country country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Languages getLanguages() {
        return this.languages;
    }

    public BusinessPlan languages(Languages languages) {
        this.setLanguages(languages);
        return this;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public String getCompanyDescription() {
        return this.companyDescription;
    }

    public BusinessPlan companyDescription(String companyDescription) {
        this.setCompanyDescription(companyDescription);
        return this;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public Double getAnticipatedProjectSize() {
        return this.anticipatedProjectSize;
    }

    public BusinessPlan anticipatedProjectSize(Double anticipatedProjectSize) {
        this.setAnticipatedProjectSize(anticipatedProjectSize);
        return this;
    }

    public void setAnticipatedProjectSize(Double anticipatedProjectSize) {
        this.anticipatedProjectSize = anticipatedProjectSize;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public BusinessPlan currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getGeneratedPresentation() {
        return this.generatedPresentation;
    }

    public BusinessPlan generatedPresentation(String generatedPresentation) {
        this.setGeneratedPresentation(generatedPresentation);
        return this;
    }

    public void setGeneratedPresentation(String generatedPresentation) {
        this.generatedPresentation = generatedPresentation;
    }

    public String getEditedPresentation() {
        return this.editedPresentation;
    }

    public BusinessPlan editedPresentation(String editedPresentation) {
        this.setEditedPresentation(editedPresentation);
        return this;
    }

    public void setEditedPresentation(String editedPresentation) {
        this.editedPresentation = editedPresentation;
    }

    public boolean isEdited() {
        return this.isEdited;
    }

    public BusinessPlan isEdited(boolean isEdited) {
        this.setEdited(isEdited);
        return this;
    }

    public void setEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }


    public String getPresentationContent() {
        return this.presentationContent;
    }

    public BusinessPlan presentationContent(String presentationContent) {
        this.setPresentationContent(presentationContent);
        return this;
    }

    public void setPresentationContent(String presentationContent) {
        this.presentationContent = presentationContent;
    }


    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessPlan)) {
            return false;
        }
        return id != null && id.equals(((BusinessPlan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessPlan{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", companyStartDate='" + getCompanyStartDate() + "'" +
            ", country='" + getCountry() + "'" +
            ", languages='" + getLanguages() + "'" +
            ", companyDescription='" + getCompanyDescription() + "'" +
            ", anticipatedProjectSize=" + getAnticipatedProjectSize() +
            ", currency='" + getCurrency() + "'" +
            ", generatedPresentation='" + getGeneratedPresentation() + "'" +
            ", presentationContent='" + getPresentationContent() + "'" +
            "}";
    }
}
