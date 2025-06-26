package com.readytoplanbe.myapp.service.dto;

import java.time.Instant;
import java.util.List;

public class BusinessPlanFinalDTO {
    private String title;
    private Instant creationDate;
    private List<AIResponseDTO> aiResponses;
    private  String companyId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    // Getters et Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public List<AIResponseDTO> getAiResponses() {
        return aiResponses;
    }

    public void setAiResponses(List<AIResponseDTO> aiResponses) {
        this.aiResponses = aiResponses;
    }

    @Override
    public String toString() {
        return "BusinessPlanFinalDTO{" +
            "title='" + title + '\'' +
            ", creationDate=" + creationDate +
            ", aiResponses=" + aiResponses +
            ", companyId='" + companyId + '\'' +
            '}';
    }
}
