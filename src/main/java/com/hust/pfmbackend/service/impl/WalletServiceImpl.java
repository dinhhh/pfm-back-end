package com.hust.pfmbackend.service.impl;

import com.hust.pfmbackend.entity.ExpenseIncome;
import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.entity.Wallet;
import com.hust.pfmbackend.model.request.NewSavingAccountRequest;
import com.hust.pfmbackend.model.request.NewWalletRequest;
import com.hust.pfmbackend.model.response.WalletResponse;
import com.hust.pfmbackend.repository.UserRepository;
import com.hust.pfmbackend.repository.WalletRepository;
import com.hust.pfmbackend.security.jwt.JwtAuthManager;
import com.hust.pfmbackend.service.WalletService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletServiceImpl implements WalletService {

    private final Logger LOGGER = LogManager.getLogger(WalletService.class);

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtAuthManager authManager;

    @Override
    public boolean save(NewWalletRequest request) {

        if (!StringUtils.hasText(request.getName())) {
            LOGGER.warn("New wallet name must not be empty");
            return false;
        }

        try {
            LOGGER.info("Starting get user detail from request");
            String userName = authManager.getUserNameByToken();
            User user = userRepository.findUserByEmail(userName);

            Wallet wallet = Wallet.builder()
                    .balance(request.getAmount())
                    .name(request.getName())
                    .description(request.getDescription())
                    .user(user)
                    .createdDate(LocalDate.now())
                    .build();

            if (request instanceof NewSavingAccountRequest newSavingAccountRequest) {
                wallet.setDayInterestRate(newSavingAccountRequest.getDayInterestRate());
                wallet.setYearInterestRate(newSavingAccountRequest.getYearInterestRate());
                wallet.setPeriod(newSavingAccountRequest.getPeriod());
                wallet.setDueDate(wallet.getCreatedDate().plusMonths(newSavingAccountRequest.getPeriod()));
            }
            walletRepository.save(wallet);
            LOGGER.info("Saved new wallet entity");
            return true;
        } catch (Exception e) {
            LOGGER.warn("Error when save new wallet");
        }

        return false;
    }

    @Override
    public WalletResponse getAllByUser() {
        try {
            LOGGER.info("Starting get user detail from request");
            String userName = authManager.getUserNameByToken();
            User user = userRepository.findUserByEmail(userName);
            List<Wallet> wallets = walletRepository.findAllByUser(user);
            for (Wallet wallet : wallets) {
                List<ExpenseIncome> rawHistory = new java.util.ArrayList<>(wallet.getHistory().stream().toList());
                rawHistory.sort(Comparator.comparing(ExpenseIncome::getCreateOn));
                wallet.setHistory(rawHistory);
            }
            long balance = wallets.stream().map(Wallet::getBalance).reduce(0L, Long::sum);
            return WalletResponse.builder()
                    .wallets(wallets.stream()
                            .filter(w -> !isSavingAccount(w))
                            .collect(Collectors.toList()))
                    .savingAccounts(wallets.stream()
                            .filter(this::isSavingAccount)
                            .collect(Collectors.toList()))
                    .balance(balance)
                    .build();
        } catch (Exception e) {
            LOGGER.warn("Error when get all wallets by user");
        }
        return null;
    }

    private boolean isSavingAccount(Wallet wallet) {
        return wallet.getYearInterestRate() != null || wallet.getDayInterestRate() != null;
    }

}
