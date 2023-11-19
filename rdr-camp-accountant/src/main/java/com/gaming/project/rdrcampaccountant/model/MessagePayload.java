package com.gaming.project.rdrcampaccountant.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MessagePayload implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("embeds")
    private List<CampAction> embeds;

    @JsonProperty("timestamp")
    private String timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CampAction> getEmbeds() {
        return embeds;
    }

    public void setEmbeds(List<CampAction> embeds) {
        this.embeds = embeds;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public MessagePayload() {}
}
