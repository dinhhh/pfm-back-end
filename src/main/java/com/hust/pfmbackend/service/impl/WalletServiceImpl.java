package com.hust.pfmbackend.service.impl;

import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.entity.Wallet;
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

import java.util.List;

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
                    .build();
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
            long balance = wallets.stream().map(Wallet::getBalance).reduce(0L, Long::sum);
            return WalletResponse.builder()
                    .wallets(wallets)
                    .balance(balance)
                    .build();
        } catch (Exception e) {
            LOGGER.warn("Error when get all wallets by user");
        }
        return null;
    }

}
