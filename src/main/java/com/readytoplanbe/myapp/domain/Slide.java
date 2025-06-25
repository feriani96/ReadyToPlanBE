package com.readytoplanbe.myapp.domain;

public class Slide {

    private String content;

    public Slide() {}

    public Slide(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "Slide{content='" + content + "'}";
    }
}
