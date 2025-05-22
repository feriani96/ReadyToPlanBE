package com.readytoplanbe.myapp.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ManualBusinessPlan.
 */
@Document(collection = "manual_business_plan")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualBusinessPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Size(max = 1000)
    @Field("description")
    private String description;

    @NotNull
    @Field("creation_date")
    private LocalDate creationDate;

    @NotNull
    @Size(max = 50)
    @Field("entrepreneur_name")
    private String entrepreneurName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ManualBusinessPlan id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ManualBusinessPlan name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ManualBusinessPlan description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public ManualBusinessPlan creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getEntrepreneurName() {
        return this.entrepreneurName;
    }

    public ManualBusinessPlan entrepreneurName(String entrepreneurName) {
        this.setEntrepreneurName(entrepreneurName);
        return this;
    }

    public void setEntrepreneurName(String entrepreneurName) {
        this.entrepreneurName = entrepreneurName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManualBusinessPlan)) {
            return false;
        }
        return id != null && id.equals(((ManualBusinessPlan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManualBusinessPlan{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", entrepreneurName='" + getEntrepreneurName() + "'" +
            "}";
    }
}
