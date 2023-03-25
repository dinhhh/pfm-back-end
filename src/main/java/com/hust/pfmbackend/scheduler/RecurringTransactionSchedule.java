package com.hust.pfmbackend.scheduler;

import com.hust.pfmbackend.entity.*;
import com.hust.pfmbackend.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.springframework.context.ApplicationContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class RecurringTransactionSchedule implements Runnable {

    private final Logger LOGGER = LogManager.getLogger(RecurringTransactionSchedule.class);

    private RecurringTransactionRepository recurringTransactionRepository;
    private NotificationRepository notificationRepository;
    private ExpenseIncomeRepository expenseIncomeRepository;
    private UserRepository userRepository;
    private WalletRepository walletRepository;

    public RecurringTransactionSchedule(ApplicationContext context) {
        this.recurringTransactionRepository = context.getBean(RecurringTransactionRepository.class);
        this.notificationRepository = context.getBean(NotificationRepository.class);
        this.expenseIncomeRepository = context.getBean(ExpenseIncomeRepository.class);
        this.userRepository = context.getBean(UserRepository.class);
        this.walletRepository = context.getBean(WalletRepository.class);
    }

    @Override
    public void run() {
        LOGGER.info("Recurring transaction schedule is starting...");
        List<RecurringTransaction> transactions = recurringTransactionRepository.findAll();
        if (transactions.isEmpty()) {
            LOGGER.info("Have 0 transactions to run");
            return;
        }
        List<Notification> notifications = new ArrayList<>();
        for (RecurringTransaction transaction : transactions) {
            Date nowDate = null;
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date today = new Date();
            try {
                nowDate = formatter.parse(formatter.format(today));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Date startDate = transaction.getStartDate();
            Date endDate = transaction.getEndDate();
            DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
            System.out.println(dateTimeComparator.compare(nowDate, startDate));
            System.out.println(dateTimeComparator.compare(nowDate, endDate));
            System.out.println("hello");
            if (!transaction.isExecuted() && dateTimeComparator.compare(nowDate, startDate) >= 0
                    && dateTimeComparator.compare(nowDate, endDate) <= 0) {
                Optional<User> userOpt = userRepository.findById(transaction.getUserNo());
                if (userOpt.isEmpty()) {
                    LOGGER.error(String.format("User no %s not found", transaction.getUserNo()));
                    return;
                }

                Optional<Wallet> optWallet = walletRepository.findById(transaction.getWalletNo());
                if (optWallet.isEmpty()) {
                    LOGGER.error(String.format("Wallet no %s not found", transaction.getWalletNo()));
                    return;
                }
                expenseIncomeRepository.save(ExpenseIncome.builder()
                        .user(userOpt.get())
                        .operationType(transaction.getOperationType())
                        .amount(transaction.getAmount())
                        .wallet(optWallet.get())
                        .createOn(new Date())
                        .categoryNo(transaction.getOperationType() == OperationType.INCOME ?
                                "60f68914-0b87-40cc-bb3b-620365a1e2bd" : "016eba3d-40b1-4c37-8409-4ebbb57c7f38") // TODO: Hard code category here for demo purpose
                        .build());
                LOGGER.info("Saved new expense or income by transaction");

                Wallet wallet = optWallet.get();
                long prevBalance = wallet.getBalance();
                if (transaction.getOperationType() == OperationType.INCOME) {
                    wallet.setBalance(prevBalance + transaction.getAmount());
                }
                if (transaction.getOperationType() == OperationType.EXPENSE) {
                    wallet.setBalance(prevBalance - transaction.getAmount());
                }
                walletRepository.save(wallet);

                transaction.setExecuted(true);
                recurringTransactionRepository.save(transaction);
                LOGGER.info("Update executed field to true");

                notifications.add(Notification.builder()
                        .userNo(transaction.getUserNo())
                        .createdOn(LocalDateTime.now())
                        .message(String.format("Ghi chép định kỳ %s đã được thực hiện", transaction.getName()))
                        .build());
            }
        }
        LOGGER.info(String.format("Loop all %d transactions", transactions.size()));
        if (!notifications.isEmpty()) {
            LOGGER.info("Start insert notifications into database");
            notificationRepository.saveAll(notifications);
            LOGGER.info("Inserted new notifications");
        }
    }

}
