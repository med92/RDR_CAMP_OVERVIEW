package com.gaming.project.rdrcampaccountant.model.dto;

import com.gaming.project.rdrcampaccountant.model.Actions;
import lombok.Data;

@Data
public class CampChestInteractions implements Comparable<CampChestInteractions>{
    private String player;
    private Double actionValue;
    private Actions actions;
    private String item;
    private String timestamp ;

    @Override
    public int compareTo(CampChestInteractions o) {
        return o.getTimestamp().compareTo(getTimestamp());
    }
}
