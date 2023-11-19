package com.gaming.project.rdrcampaccountant.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gaming.project.rdrcampaccountant.mapper.MessagePayLoadMapper;
import com.gaming.project.rdrcampaccountant.model.Actions;
import com.gaming.project.rdrcampaccountant.model.MessagePayload;
import com.gaming.project.rdrcampaccountant.model.dto.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MessagePayloadServiceImpl implements MessagePayloadService {

    @Autowired
    private MessagePayLoadMapper messagePayLoadMapper;

    @Override
    public void writeObjectsToFile(List<MessagePayload> messagePayloads) throws IOException {

        FileOutputStream fos = new FileOutputStream("/resources.json");
        OutputStreamWriter ows = new OutputStreamWriter(fos);
        ows.write("[");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        for (int i = 0; i < messagePayloads.size(); i++) {
            if (i != 0) {
                ows.write(",");
            }

            String json = ow.writeValueAsString(messagePayloads.get(i));
            ows.write(json);
        }
        ows.write("]");
        ows.flush();

    }

    @Override
    public List<MessagePayload> readMessagesFromFile() throws IOException {
        String fileName = "src/main/resources/students.json";
        Path path = Paths.get(fileName);

        try {
            // create Gson instance
            Gson gson = new Gson();

            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get("/resources.json"));

            // convert JSON array to list of users
            List<MessagePayload> messagePayloads = new Gson().fromJson(reader, new TypeToken<List<MessagePayload>>() {}.getType());


            // close reader
            reader.close();
            return messagePayloads;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UserDonations> calculateUserContribution(List<MessagePayload> messagePayloads) throws IOException {
        return null;
    }

    private List<MessagePayload> updateFileWithNewFeed(List<MessagePayload> messagePayloads) throws IOException {
        List<MessagePayload> oldMessages = readMessagesFromFile() ;
        messagePayloads.stream().forEach(x -> {
            Optional<MessagePayload> opt = oldMessages.stream().filter(ox -> ox.getId().equals(x.getId())).findFirst();
            if(opt.isEmpty()){
                oldMessages.add(x) ;
            };
        });
        writeObjectsToFile(oldMessages);
        return  oldMessages ;
    }

    @Override
    public PlayersStats calculateUserDonationsNDelivery(List<MessagePayload> messagePayloads) throws IOException {
        List<MessagePayload> updatedMsgList = updateFileWithNewFeed(messagePayloads) ;
        List<MessagePayloadDto> messagePayloadDtos = new ArrayList<>(updatedMsgList.stream().map(messagePayLoadMapper::fromPojoToDto).toList());
        Collections.sort(messagePayloadDtos) ;
        PlayersStats playersStats = new PlayersStats() ;
        playersStats.setPlayersDelivery(calculateUserContribution(messagePayloadDtos,Actions.DELIVERY));
        playersStats.setPlayerDonations(calculateUserContribution(messagePayloadDtos,Actions.DONATION));
        playersStats.calculateCurrentSaleStatus() ;
        playersStats.setAllTimeDonationContribution(calculateUserAllTimeDonationContribution(messagePayloadDtos));
        playersStats.setUserLedgerOperations(ledgerOperations(messagePayloadDtos));
        playersStats.calculatePlayerTotalLedgerContribution() ;
        playersStats.setCampChestInteractions(getUserInteractionWithCampChest(messagePayloadDtos));
        return playersStats ;
    }

    @Override
    public List<UserLedgerOperation> ledgerOperations (List<MessagePayloadDto> messagePayloadDtos) throws IOException {

        List<UserLedgerOperation> userLedgerOperations = new ArrayList<>() ;
        for (MessagePayloadDto dto : messagePayloadDtos) {
            if (
                    null != dto.getEmbeds() &&
                            (dto.getEmbeds().getActions().equals(Actions.DEPOSIT) || dto.getEmbeds().getActions().equals(Actions.WITHDRAWAL) || dto.getEmbeds().getActions().equals(Actions.ADD_TAX))
            ) {
                UserLedgerOperation trans = new UserLedgerOperation() ;
                trans.setUser(dto.getEmbeds().getTitle());
                trans.setTransactionDate(dto.getTimestamp());
                trans.setAmount(dto.getEmbeds().getActionValue());
                trans.setTransaction(dto.getEmbeds().getActions());
                userLedgerOperations.add(trans) ;
            }
        }
        Collections.sort(userLedgerOperations);
        return userLedgerOperations ;
    }

    public List<UserDonations> calculateUserContribution(List<MessagePayloadDto> messagePayloadDtos, Actions contributionType) throws IOException {
        Map<String, Map<String, Double>> salePlayerContribution = new HashMap<>();
        Map<String, Double> playerDonationsDeliveryMap = new HashMap<>();
        Integer saleFailIndex = 1;
        for (MessagePayloadDto dto : messagePayloadDtos) {
            if (
                    null != dto.getEmbeds() &&
                            (dto.getEmbeds().getActions().equals(contributionType) )
            ) {
                if (null == playerDonationsDeliveryMap.get(dto.getEmbeds().getTitle())) {
                    playerDonationsDeliveryMap.put(dto.getEmbeds().getTitle(), dto.getEmbeds().getActionValue());
                } else {
                    playerDonationsDeliveryMap.replace(
                            dto.getEmbeds().getTitle(),
                            playerDonationsDeliveryMap.get(dto.getEmbeds().getTitle()) + dto.getEmbeds().getActionValue()
                    );
                }
            } else if (null != dto.getEmbeds() && dto.getEmbeds().getActions().equals(Actions.SALE)) {
                salePlayerContribution.put("SALE " + saleFailIndex, playerDonationsDeliveryMap);
                saleFailIndex++;
                playerDonationsDeliveryMap = new HashMap<>();
            }
        }
        salePlayerContribution.put("CURRENT SALE", playerDonationsDeliveryMap);
        List<UserDonations> userDonationsSale = new ArrayList<>() ;
        for (String key : salePlayerContribution.keySet()) {
            UserDonations userDonations1 = new UserDonations() ;
            userDonations1.setSalesName(key);
            for (String keyUserDonation : salePlayerContribution.get(key).keySet()) {
                userDonations1.getLabels().add(keyUserDonation);
                userDonations1.getData().add(salePlayerContribution.get(key).get(keyUserDonation));
            }
            userDonationsSale.add(userDonations1) ;
        }
        return userDonationsSale;
    }

    public List<UserDonations> calculateUserAllTimeDonationContribution(List<MessagePayloadDto> messagePayloadDtos) throws IOException {
        Map<String, Map<String, Double>> salePlayerContribution = new HashMap<>();
        Map<String, Double> playerDonationsDeliveryMap = new HashMap<>();
        Integer saleFailIndex = 1;
        for (MessagePayloadDto dto : messagePayloadDtos) {
            if (
                    null != dto.getEmbeds() &&
                            (dto.getEmbeds().getActions().equals(Actions.DONATION) )
            ) {
                if (null == playerDonationsDeliveryMap.get(dto.getEmbeds().getTitle())) {
                    playerDonationsDeliveryMap.put(dto.getEmbeds().getTitle(), dto.getEmbeds().getActionValue());
                } else {
                    playerDonationsDeliveryMap.replace(
                            dto.getEmbeds().getTitle(),
                            playerDonationsDeliveryMap.get(dto.getEmbeds().getTitle()) + dto.getEmbeds().getActionValue()
                    );
                }
            }
        }
        salePlayerContribution.put("ALL TIME CONTRIBUTION",playerDonationsDeliveryMap) ;
        List<UserDonations> userDonationsSale = new ArrayList<>() ;
        for (String key : salePlayerContribution.keySet()) {
            UserDonations userDonations1 = new UserDonations() ;
            userDonations1.setSalesName(key);
            for (String keyUserDonation : salePlayerContribution.get(key).keySet()) {
                userDonations1.getLabels().add(keyUserDonation);
                userDonations1.getData().add(salePlayerContribution.get(key).get(keyUserDonation));
            }
            userDonationsSale.add(userDonations1) ;
        }
        return userDonationsSale;
    }

    public List<CampChestInteractions> getUserInteractionWithCampChest(List<MessagePayloadDto> messagePayloadDtos) throws IOException {

        List<CampChestInteractions> campChestInteractions = new ArrayList<>() ;
        for (MessagePayloadDto dto : messagePayloadDtos) {
            if (
                    null != dto.getEmbeds() &&
                            (dto.getEmbeds().getActions().equals(Actions.DEPOSIT_ITEM) || dto.getEmbeds().getActions().equals(Actions.WITHDRAW_ITEM))
            ) {
                CampChestInteractions trans = new CampChestInteractions() ;
                trans.setPlayer(dto.getEmbeds().getTitle());
                trans.setActionValue(dto.getEmbeds().getActionValue());
                trans.setActions(dto.getEmbeds().getActions());
                trans.setItem(dto.getEmbeds().getItem());
                trans.setTimestamp(dto.getTimestamp());
                campChestInteractions.add(trans) ;
            }
        }
        Collections.sort(campChestInteractions);
        return campChestInteractions ;
    }
}
