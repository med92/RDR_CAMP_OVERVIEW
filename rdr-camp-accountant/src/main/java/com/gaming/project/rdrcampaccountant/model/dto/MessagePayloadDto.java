package com.gaming.project.rdrcampaccountant.model.dto;

import lombok.Data;

@Data
public class MessagePayloadDto implements Comparable<MessagePayloadDto>{

    private String id;
    private String content;
    private CampActionDto embeds;

    private String timestamp;


    @Override
    public int compareTo(MessagePayloadDto o) {
        return getTimestamp().compareTo(o.getTimestamp());
    }
}
