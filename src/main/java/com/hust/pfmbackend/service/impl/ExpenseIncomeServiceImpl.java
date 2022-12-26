package com.hust.pfmbackend.service.impl;

import com.hust.pfmbackend.entity.ExpenseIncome;
import com.hust.pfmbackend.entity.OperationType;
import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.entity.Wallet;
import com.hust.pfmbackend.model.request.ExpenseIncomeRequest;
import com.hust.pfmbackend.repository.ExpenseIncomeRepository;
import com.hust.pfmbackend.repository.WalletRepository;
import com.hust.pfmbackend.security.jwt.JwtAuthManager;
import com.hust.pfmbackend.service.ExpenseIncomeService;
import com.hust.pfmbackend.utils.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseIncomeServiceImpl implements ExpenseIncomeService {

    private final Logger LOGGER = LogManager.getLogger(ExpenseIncomeService.class);

    @Autowired
    private JwtAuthManager authManager;

    @Autowired
    private ExpenseIncomeRepository expenseIncomeRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public boolean save(ExpenseIncomeRequest request) {
        if ( !Validator.isValidObject(request) ) {
            LOGGER.warn("Request is not valid by constraint");
            return false;
        }

        try {
            LOGGER.info("Starting save new expense income");
            User user = authManager.getUserByToken();
            Wallet wallet = walletRepository.findById(request.getWalletNo()).orElseThrow();
            long newBalance = wallet.getBalance() - request.getAmount();
            wallet.setBalance(newBalance);
            walletRepository.save(wallet);
            LOGGER.info(String.format("Updated wallet balance from to %d", newBalance));
            ExpenseIncome ei = ExpenseIncome.builder()
                    .createOn(request.getCreatedOn())
                    .amount(request.getAmount())
                    .description(request.getDescription())
                    .categoryNo(request.getCategoryNo())
                    .wallet(wallet)
                    .user(user)
                    .operationType(OperationType.findByCode(request.getOperationCode()))
                    .build();
            expenseIncomeRepository.save(ei);
            LOGGER.info("Saved new expense income entity");
            return true;
        } catch (Exception e) {
            LOGGER.error("Error when save new expense income request");
        }

        return false;
    }
}
