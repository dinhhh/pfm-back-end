package com.hust.pfmbackend.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class NewSavingAccountRequest extends NewWalletRequest {

    private BigDecimal yearInterestRate;

    private BigDecimal dayInterestRate;

    private long period;
}
