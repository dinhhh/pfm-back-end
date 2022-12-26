package com.hust.pfmbackend.service;

import com.hust.pfmbackend.model.request.NewWalletRequest;
import com.hust.pfmbackend.model.response.WalletResponse;

public interface WalletService {

    boolean save(NewWalletRequest request);

    WalletResponse getAllByUser();

}
