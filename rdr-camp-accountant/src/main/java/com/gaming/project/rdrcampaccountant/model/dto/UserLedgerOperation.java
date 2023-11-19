package com.gaming.project.rdrcampaccountant.model.dto;

import com.gaming.project.rdrcampaccountant.model.Actions;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserLedgerOperation implements Comparable<UserLedgerOperation>{
    private String user ;
    private Double Amount ;
    private Actions transaction ;
    private String transactionDate ;

    @Override
    public int compareTo(UserLedgerOperation o) {
        return o.getTransactionDate().compareTo(getTransactionDate());
    }
}
