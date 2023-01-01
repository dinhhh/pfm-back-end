package com.hust.pfmbackend.service.impl;

import com.hust.pfmbackend.entity.*;
import com.hust.pfmbackend.model.response.statistic.ExpenseIncomeResponse;
import com.hust.pfmbackend.model.response.statistic.GeneralStatisticResponse;
import com.hust.pfmbackend.repository.DebtRepository;
import com.hust.pfmbackend.repository.ExpenseIncomeRepository;
import com.hust.pfmbackend.repository.WalletRepository;
import com.hust.pfmbackend.security.jwt.JwtAuthManager;
import com.hust.pfmbackend.service.StatisticService;
import com.hust.pfmbackend.service.graphbuilder.GraphDataBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final Logger LOGGER = LogManager.getLogger(StatisticServiceImpl.class);

    @Autowired
    private JwtAuthManager authManager;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private ExpenseIncomeRepository expenseIncomeRepository;

    @Override
    public GeneralStatisticResponse getGeneral() {
        try {
            LOGGER.info("Start get general statistic for user");
            User user = authManager.getUserByToken();
            List<Wallet> wallets = walletRepository.findAllByUser(user);
            if (wallets.isEmpty()) {
                LOGGER.warn(String.format("User %s dont have any wallet to statistic", user.getEmail()));
                return null;
            }

            long currentHaving = wallets.stream()
                    .map(Wallet::getBalance)
                    .reduce(0L, Long::sum);

            long currentBorrow = debtRepository.findAllByUserNoAndOperationType(user.getUserNo(), OperationType.BORROW)
                    .stream()
                    .map(Debt::getAmount)
                    .reduce(0L, Long::sum);

            long currentLend = debtRepository.findAllByUserNoAndOperationType(user.getUserNo(), OperationType.LEND)
                    .stream()
                    .map(Debt::getAmount)
                    .reduce(0L, Long::sum);

            long currentBalance = currentHaving - currentBorrow + currentLend;
            LOGGER.info("Calculated field for statistic response");

            return GeneralStatisticResponse.builder()
                    .currentBalance(currentBalance)
                    .currentHave(currentHaving)
                    .currentBorrow(currentBorrow)
                    .currentLend(currentLend)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error when get all general");
        }
        return null;
    }

    @Override
    public ExpenseIncomeResponse getExpenseIncomeGraphData() {
        try {
            LOGGER.info("Start get getExpenseIncomeGraphData for user");
            User user = authManager.getUserByToken();

            List<ExpenseIncome> expenses = expenseIncomeRepository.findAllByUserAndOperationType(user, OperationType.EXPENSE);
            List<ExpenseIncome> incomes = expenseIncomeRepository.findAllByUserAndOperationType(user, OperationType.INCOME);
            List<ExpenseIncome> all = new ArrayList<>();
            all.addAll(expenses);
            all.addAll(incomes);

            GraphDataBuilder dataBuilder = new GraphDataBuilder(all);
            LOGGER.info("Built response for expense income graph");
            return ExpenseIncomeResponse.builder()
                    .month(dataBuilder.buildMonthData())
                    .quarter(dataBuilder.buildQuarterData())
                    .year(dataBuilder.buildYearData())
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error when getExpenseIncomeGraphData");
        }

        return null;
    }

    @Override
    public ExpenseIncomeResponse getExpenseGraphData() {
        try {
            LOGGER.info("Start get getExpenseGraphData for user");
            User user = authManager.getUserByToken();
            List<ExpenseIncome> expenses = expenseIncomeRepository.findAllByUserAndOperationType(user, OperationType.EXPENSE);
            GraphDataBuilder dataBuilder = new GraphDataBuilder(expenses);
            LOGGER.info("Built response for expense graph");
            return ExpenseIncomeResponse.builder()
                    .month(dataBuilder.buildMonthData())
                    .quarter(dataBuilder.buildQuarterData())
                    .year(dataBuilder.buildYearData())
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error when getExpenseIncomeGraphData");
        }

        return null;
    }

    @Override
    public ExpenseIncomeResponse getIncomeGraphData() {
        try {
            LOGGER.info("Start get getIncomeGraphData for user");
            User user = authManager.getUserByToken();
            List<ExpenseIncome> incomes = expenseIncomeRepository.findAllByUserAndOperationType(user, OperationType.INCOME);
            GraphDataBuilder dataBuilder = new GraphDataBuilder(incomes);
            LOGGER.info("Built response for income graph");
            return ExpenseIncomeResponse.builder()
                    .month(dataBuilder.buildMonthData())
                    .quarter(dataBuilder.buildQuarterData())
                    .year(dataBuilder.buildYearData())
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error when getExpenseIncomeGraphData");
        }

        return null;
    }

}
