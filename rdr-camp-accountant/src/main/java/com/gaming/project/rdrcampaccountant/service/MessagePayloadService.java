package com.gaming.project.rdrcampaccountant.service;

import com.gaming.project.rdrcampaccountant.model.MessagePayload;
import com.gaming.project.rdrcampaccountant.model.dto.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MessagePayloadService {
    void writeObjectsToFile(List<MessagePayload> messagePayloads) throws IOException;
    List<MessagePayload> readMessagesFromFile() throws IOException;

    List<UserDonations> calculateUserContribution(List<MessagePayload> messagePayloads) throws IOException;
    PlayersStats calculateUserDonationsNDelivery(List<MessagePayload> messagePayloads) throws IOException;
    List<UserLedgerOperation> ledgerOperations (List<MessagePayloadDto> messagePayloads) throws IOException;
}
