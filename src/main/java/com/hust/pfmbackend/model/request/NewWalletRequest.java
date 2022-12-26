package com.hust.pfmbackend.model.request;

import lombok.Data;

@Data
public class NewWalletRequest {

    private String name;

    private long amount;

    private String description;

}
