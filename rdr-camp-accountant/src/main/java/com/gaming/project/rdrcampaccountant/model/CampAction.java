package com.gaming.project.rdrcampaccountant.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CampAction implements Serializable {

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CampAction() {}
}
