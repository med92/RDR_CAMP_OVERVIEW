package com.gaming.project.rdrcampaccountant.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDonations {
    private List<String> labels = new ArrayList<>();
    private List<Double> data = new ArrayList<>();
    private String salesName ;
}
