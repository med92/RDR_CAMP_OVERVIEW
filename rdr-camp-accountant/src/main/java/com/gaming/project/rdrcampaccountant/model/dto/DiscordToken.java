package com.gaming.project.rdrcampaccountant.model.dto;

import lombok.Data;

@Data
public class DiscordToken {
    private String token ;

    public DiscordToken(String token) {
        this.token = token;
    }
}
