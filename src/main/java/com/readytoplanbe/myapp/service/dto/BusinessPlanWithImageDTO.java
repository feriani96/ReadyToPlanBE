package com.readytoplanbe.myapp.service.dto;

public class BusinessPlanWithImageDTO {
    private String presentation;
    private String coverImageUrl;

    // Ajoutez ce constructeur
    public BusinessPlanWithImageDTO(String presentation, String coverImageUrl) {
        this.presentation = presentation;
        this.coverImageUrl = coverImageUrl;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
}
