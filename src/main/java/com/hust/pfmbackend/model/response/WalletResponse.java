package com.hust.pfmbackend.model.response;

import com.hust.pfmbackend.entity.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletResponse {

    private long balance;

    private List<Wallet> wallets;

    private List<Wallet> savingAccounts;

}
