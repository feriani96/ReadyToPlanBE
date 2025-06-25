package com.readytoplanbe.myapp.web.rest.vm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SlideUpdateRequest {

    @NotNull
    private Integer slideIndex;

    @NotNull
    @Size(min = 1, max = 10000)
    private String newContent;

    public SlideUpdateRequest() {}

    public Integer getSlideIndex() {
        return slideIndex;
    }

    public void setSlideIndex(Integer slideIndex) {
        this.slideIndex = slideIndex;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }
}
