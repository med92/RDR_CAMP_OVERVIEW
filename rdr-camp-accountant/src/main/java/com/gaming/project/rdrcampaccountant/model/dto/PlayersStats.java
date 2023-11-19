package com.gaming.project.rdrcampaccountant.model.dto;

import com.gaming.project.rdrcampaccountant.model.Actions;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PlayersStats {
    private List<UserDonations> playerDonations ;
    private List<UserDonations> playersDelivery ;
    private List<UserDonations> allTimeDonationContribution ;
    private List<UserLedgerOperation> userLedgerOperations ;
    private Double currentSaleStatus;
    private List<PlayerTotalLedgerContribution> playerTotalLedgerContributions ;
    private List<CampChestInteractions> campChestInteractions ;

    public void calculateCurrentSaleStatus(){
        this.currentSaleStatus =  this.playerDonations.stream()
                .filter(x -> x.getSalesName().equals("CURRENT SALE")).findFirst()
                .map(x -> x.getData().stream().reduce((double) 0, Double::sum)).orElseGet(() -> (double) 0 ) +
                this.playersDelivery.stream()
                        .filter(x -> x.getSalesName().equals("CURRENT SALE")).findFirst()
                        .map(x -> x.getData().stream().reduce((double) 0, Double::sum)).orElseGet(() -> (double) 0 );
    }

    public void calculatePlayerTotalLedgerContribution(){
        Map<String, Map<String,Double>> ledgerAndTax = new HashMap<>() ;
        userLedgerOperations.forEach(lc -> {
            ledgerAndTax.computeIfAbsent(lc.getUser(), k -> new HashMap<>());
            if(lc.getTransaction().equals(Actions.WITHDRAWAL)){
                ledgerAndTax.get(lc.getUser()).computeIfAbsent("LEDGER", k -> lc.getAmount() * -1) ;
                ledgerAndTax.get(lc.getUser()).computeIfPresent("LEDGER", (s, aDouble) -> aDouble - lc.getAmount()) ;
            }else if(lc.getTransaction().equals(Actions.DEPOSIT)){
                ledgerAndTax.get(lc.getUser()).computeIfAbsent("LEDGER", k -> lc.getAmount() ) ;
                ledgerAndTax.get(lc.getUser()).computeIfPresent("LEDGER", (s, aDouble) -> aDouble + lc.getAmount()) ;
            }else if(lc.getTransaction().equals(Actions.ADD_TAX)){
                ledgerAndTax.get(lc.getUser()).computeIfAbsent("TAX", k -> lc.getAmount() ) ;
                ledgerAndTax.get(lc.getUser()).computeIfPresent("TAX", (s, aDouble) -> aDouble + lc.getAmount()) ;
            }
        });
        this.playerTotalLedgerContributions = new ArrayList<>() ;
        ledgerAndTax.forEach((k, v) -> {
            PlayerTotalLedgerContribution plc = new PlayerTotalLedgerContribution() ;
            plc.setPlayer(k);
            plc.setLedgerContribution(v.get("LEDGER"));
            plc.setTaxContribution(v.get("TAX"));
            this.playerTotalLedgerContributions.add(plc) ;
        });


    }


}
