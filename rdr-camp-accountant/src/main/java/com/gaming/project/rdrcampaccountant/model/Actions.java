package com.gaming.project.rdrcampaccountant.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Actions {
    DELIVERY("Delivered Supplies"),
    DONATION("Donated"),
    WITHDRAWAL("Withdrew from clan ledger"),
    DEPOSIT("Deposited to clan ledger"),
    ADD_TAX("Added Money To Tax Ledger"),
    SALE("Made a Sale Of 100 Of Stock For"),
    FAIL_SALE_OR_DELIVERY("FAILED TO SALE OR DELIVERY"),
    WITHDRAW_ITEM("Withdraw item"),
    DEPOSIT_ITEM("Deposited item");

    private final String value;

    Actions(final String value) {
        this.value = value;
    }

    private static final Map<String, Actions> reverseMap = Stream
        .of(Actions.values())
        .collect(Collectors.toMap(Actions::getValue, val -> val));

    @JsonValue
    public String getValue() {
        return this.value;
    }

    public static Actions fromStringToValue(final String value) {
        return reverseMap.getOrDefault(value, null);
    }
}
