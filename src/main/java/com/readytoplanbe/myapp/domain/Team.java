package com.readytoplanbe.myapp.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Team.
 */
@Document(collection = "team")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("role")
    private String role;

    @Field("competance")
    private String competance;

    @Field("email")
    private String email;

    @Field("salaire")
    private Long salaire;

    @Field("currency")
    private String currency;
    @DBRef
    private Company company;
    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getId() {
        return this.id;
    }

    public Team id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Team name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return this.role;
    }

    public Team role(String role) {
        this.setRole(role);
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCompetance() {
        return this.competance;
    }

    public Team competance(String competance) {
        this.setCompetance(competance);
        return this;
    }

    public void setCompetance(String competance) {
        this.competance = competance;
    }

    public String getEmail() {
        return this.email;
    }

    public Team email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getSalaire() {
        return this.salaire;
    }

    public Team salaire(Long salaire) {
        this.setSalaire(salaire);
        return this;
    }

    public void setSalaire(Long salaire) {
        this.salaire = salaire;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Team currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Team)) {
            return false;
        }
        return id != null && id.equals(((Team) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Team{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", role='" + getRole() + "'" +
            ", competance='" + getCompetance() + "'" +
            ", email='" + getEmail() + "'" +
            ", salaire=" + getSalaire() +
            ", currency='" + getCurrency() + "'" +
            "}";
    }
}
