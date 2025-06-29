package com.readytoplanbe.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.readytoplanbe.myapp.domain.Team} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeamDTO implements Serializable {

    private String id;

    private String name;

    private String role;

    private String competance;

    private String email;

    private Long salaire;

    private String currency;
    private String companyId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCompetance() {
        return competance;
    }

    public void setCompetance(String competance) {
        this.competance = competance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getSalaire() {
        return salaire;
    }

    public void setSalaire(Long salaire) {
        this.salaire = salaire;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamDTO)) {
            return false;
        }

        TeamDTO teamDTO = (TeamDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teamDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", role='" + getRole() + "'" +
            ", competance='" + getCompetance() + "'" +
            ", email='" + getEmail() + "'" +
            ", salaire=" + getSalaire() +
            ", currency='" + getCurrency() + "'" +
            "}";
    }
}
