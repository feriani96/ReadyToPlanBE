package com.readytoplanbe.myapp.domain;

import com.readytoplanbe.myapp.domain.enumeration.EntityType;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A AIGeneratedResponse.
 */
@Document(collection = "ai_generated_response")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AIGeneratedResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("entity_id")
    private String entityId;

    @NotNull
    @Field("entity_type")
    private EntityType entityType;

    @NotNull
    @Field("prompt")
    private String prompt;

    @NotNull
    @Field("ai_response")
    private String aiResponse;


    @Field("business_plan_id")
    private String businessPlanId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public AIGeneratedResponse id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityId() {
        return this.entityId;
    }

    public AIGeneratedResponse entityId(String entityId) {
        this.setEntityId(entityId);
        return this;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public AIGeneratedResponse entityType(EntityType entityType) {
        this.setEntityType(entityType);
        return this;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public AIGeneratedResponse prompt(String prompt) {
        this.setPrompt(prompt);
        return this;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getAiResponse() {
        return this.aiResponse;
    }

    public AIGeneratedResponse aiResponse(String aiResponse) {
        this.setAiResponse(aiResponse);
        return this;
    }

    public void setAiResponse(String aiResponse) {
        this.aiResponse = aiResponse;
    }

    public String getBusinessPlanId() {
        return this.businessPlanId;
    }

    public AIGeneratedResponse businessPlanId(String businessPlanId) {
        this.setBusinessPlanId(businessPlanId);
        return this;
    }

    public void setBusinessPlanId(String businessPlanId) {
        this.businessPlanId = businessPlanId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AIGeneratedResponse)) {
            return false;
        }
        return id != null && id.equals(((AIGeneratedResponse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AIGeneratedResponse{" +
            "id=" + getId() +
            ", entityId='" + getEntityId() + "'" +
            ", entityType='" + getEntityType() + "'" +
            ", prompt='" + getPrompt() + "'" +
            ", aiResponse='" + getAiResponse() + "'" +
            ", businessPlanId='" + getBusinessPlanId() + "'" +
            "}";
    }
}
