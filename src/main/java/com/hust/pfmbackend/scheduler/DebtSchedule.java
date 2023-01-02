package com.hust.pfmbackend.scheduler;

import com.hust.pfmbackend.entity.Debt;
import com.hust.pfmbackend.entity.Notification;
import com.hust.pfmbackend.entity.OperationType;
import com.hust.pfmbackend.entity.UserDebtInfo;
import com.hust.pfmbackend.repository.DebtRepository;
import com.hust.pfmbackend.repository.NotificationRepository;
import com.hust.pfmbackend.repository.UserDebtInfoRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTimeComparator;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.util.*;

public class DebtSchedule implements Runnable {

    private final Logger LOGGER = LogManager.getLogger(DebtSchedule.class);
    private final Map<OperationType, String> mapMessageFormat = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(OperationType.BORROW, "Tới hạn trả tiền cho %s (%s VNĐ)"),
            new AbstractMap.SimpleEntry<>(OperationType.LEND, "Tới hạn %s trả tiền (%s VNĐ)")
    );

    private final DebtRepository debtRepository;
    private final NotificationRepository notificationRepository;
    private final UserDebtInfoRepository userDebtInfoRepository;

    public DebtSchedule(ApplicationContext context) {
        this.debtRepository = context.getBean(DebtRepository.class);
        this.notificationRepository = context.getBean(NotificationRepository.class);
        this.userDebtInfoRepository = context.getBean(UserDebtInfoRepository.class);
    }

    @Override
    public void run() {
        LOGGER.info("Debt scheduler is starting....");
        List<Debt> debts = debtRepository.findAll();
        Date nowDate = new Date();
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        List<Notification> notifications = new ArrayList<>();
        LOGGER.info("Start loop all debts");
        for (Debt debt : debts) {
            if (!debt.isNotified() && dateTimeComparator.compare(nowDate, debt.getExpectCollectDate()) == 0) {
                String userDebtInfoNo = debt.getUserDebtInfoNo();
                Optional<UserDebtInfo> userDebtInfoOpt = userDebtInfoRepository.findById(userDebtInfoNo);
                if (userDebtInfoOpt.isEmpty()) {
                    LOGGER.error(String.format("User debt info no %s not found", userDebtInfoNo));
                    continue;
                }

                debt.setNotified(true);
                debtRepository.save(debt);
                LOGGER.info("Change Debt notified field to true");

                notifications.add(Notification.builder()
                        .userNo(debt.getUserNo())
                        .createdOn(nowLocalDateTime)
                        .message(String.format(mapMessageFormat.get(debt.getOperationType()),
                                userDebtInfoOpt.get().getName(),
                                debt.getAmount()))
                        .build());
                LOGGER.info("Notification added");
            }
        }
        LOGGER.info(String.format("Loop all %d debts", debts.size()));
        if (!notifications.isEmpty()) {
            LOGGER.info("Start insert notifications into database");
            notificationRepository.saveAll(notifications);
            LOGGER.info("Inserted new notifications");
        }
        LOGGER.info("Debt scheduler done...");
    }

}
