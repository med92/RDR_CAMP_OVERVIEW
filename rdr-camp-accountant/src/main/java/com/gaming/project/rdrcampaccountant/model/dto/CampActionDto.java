package com.gaming.project.rdrcampaccountant.model.dto;

import com.gaming.project.rdrcampaccountant.model.Actions;
import lombok.Data;

@Data
public class CampActionDto {

    private String title;
    private Double actionValue;
    private Actions actions;
    private String item;

}
