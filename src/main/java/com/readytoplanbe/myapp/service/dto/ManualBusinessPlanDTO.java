package com.readytoplanbe.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.readytoplanbe.myapp.domain.ManualBusinessPlan} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualBusinessPlanDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    @NotNull
    @Size(max = 1000)
    private String description;

    @NotNull
    private LocalDate creationDate;

    @NotNull
    @Size(max = 50)
    private String entrepreneurName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getEntrepreneurName() {
        return entrepreneurName;
    }

    public void setEntrepreneurName(String entrepreneurName) {
        this.entrepreneurName = entrepreneurName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManualBusinessPlanDTO)) {
            return false;
        }

        ManualBusinessPlanDTO manualBusinessPlanDTO = (ManualBusinessPlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, manualBusinessPlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManualBusinessPlanDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", entrepreneurName='" + getEntrepreneurName() + "'" +
            "}";
    }
}
