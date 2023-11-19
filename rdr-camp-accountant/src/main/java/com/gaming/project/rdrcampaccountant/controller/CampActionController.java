package com.gaming.project.rdrcampaccountant.controller;


import com.gaming.project.rdrcampaccountant.mapper.MessagePayLoadMapper;
import com.gaming.project.rdrcampaccountant.model.MessagePayload;
import com.gaming.project.rdrcampaccountant.model.dto.PlayersStats;
import com.gaming.project.rdrcampaccountant.model.dto.UserDonations;
import com.gaming.project.rdrcampaccountant.service.MessagePayloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/camp/accountant")
public class CampActionController {

    @Autowired
    private MessagePayloadService messagePayloadService;

    private final Logger log = LoggerFactory.getLogger(CampActionController.class);

    @Value("${discord.chanel.token:ODI1NzI5NDAyNzA0ODg3ODI4.GJ_GLm.71jVX1jKej2BvZkhVpLZQZgu3J_EKLj0uzTxT4}")
    private  String token = "" ;
    @Value("${discord.chanel.url:'https://discord.com/api/channels/1173963342503747697/messages?limit=100'}")
    private String url ;
    private static class AccountResourceException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private AccountResourceException(String message) {
            super(message);
        }
    }


    @GetMapping
    public ResponseEntity<PlayersStats> getAccount() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.add("Authorization",token );
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<MessagePayload[]> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            MessagePayload[].class
        );

        return ResponseEntity.ok().body(messagePayloadService.calculateUserDonationsNDelivery(List.of(response.getBody())));
    }
}
