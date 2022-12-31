package com.hust.pfmbackend.service.impl;

import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.entity.Wallet;
import com.hust.pfmbackend.model.response.statistic.GeneralStatisticResponse;
import com.hust.pfmbackend.repository.WalletRepository;
import com.hust.pfmbackend.security.jwt.JwtAuthManager;
import com.hust.pfmbackend.service.StatisticService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final Logger LOGGER = LogManager.getLogger(StatisticServiceImpl.class);

    @Autowired
    private JwtAuthManager authManager;

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public GeneralStatisticResponse getGeneral() {
        try {
            User user = authManager.getUserByToken();
            List<Wallet> wallets = walletRepository.findAllByUser(user);
            if (wallets.isEmpty()) {
                LOGGER.warn(String.format("User %s dont have any wallet to statistic", user.getEmail()));
                return null;
            }

            long currentBalance = wallets.stream()
                    .map(Wallet::getBalance)
                    .reduce(0L, Long::sum);


            return null; // TODO: build response latter, after me finish debt table
        } catch (Exception e) {
            LOGGER.error("Error when get all general");
        }
        return null;
    }

}
