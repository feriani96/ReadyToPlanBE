package com.readytoplanbe.myapp.service.dto;

public class AIResponseDTO {
    private String entityType;
    private String aiResponse;

    public AIResponseDTO(String entityType, String aiResponse) {
        this.entityType = entityType;
        this.aiResponse = aiResponse;
    }

    // Getters et Setters
    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getAiResponse() {
        return aiResponse;
    }

    public void setAiResponse(String aiResponse) {
        this.aiResponse = aiResponse;
    }

}
