package com.hust.pfmbackend.scheduler;

import com.hust.pfmbackend.entity.ExpenseIncome;
import com.hust.pfmbackend.entity.Notification;
import com.hust.pfmbackend.entity.RecurringTransaction;
import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.repository.ExpenseIncomeRepository;
import com.hust.pfmbackend.repository.NotificationRepository;
import com.hust.pfmbackend.repository.RecurringTransactionRepository;
import com.hust.pfmbackend.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTimeComparator;
import org.springframework.context.ApplicationContext;

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

    public RecurringTransactionSchedule(ApplicationContext context) {
        this.recurringTransactionRepository = context.getBean(RecurringTransactionRepository.class);
        this.notificationRepository = context.getBean(NotificationRepository.class);
        this.expenseIncomeRepository = context.getBean(ExpenseIncomeRepository.class);
        this.userRepository = context.getBean(UserRepository.class);
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
            Date nowDate = new Date();
            Date startDate = transaction.getStartDate();
            Date endDate = transaction.getEndDate();
            DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
            if (!transaction.isExecuted() && dateTimeComparator.compare(nowDate, startDate) < 0
                    && dateTimeComparator.compare(nowDate, endDate) > 0) {
                Optional<User> userOpt = userRepository.findById(transaction.getUserNo());
                if (userOpt.isEmpty()) {
                    LOGGER.error(String.format("User no %s not found", transaction.getUserNo()));
                    return;
                }
                expenseIncomeRepository.save(ExpenseIncome.builder()
                        .user(userOpt.get())
                        .operationType(transaction.getOperationType())
                        .amount(transaction.getAmount())
                        .build());
                LOGGER.info("Saved new expense or income by transaction");

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
