package com.hust.pfmbackend.service.impl;

import com.hust.pfmbackend.entity.*;
import com.hust.pfmbackend.model.response.NotificationResponse;
import com.hust.pfmbackend.repository.*;
import com.hust.pfmbackend.security.jwt.JwtAuthManager;
import com.hust.pfmbackend.service.NotificationService;
import com.hust.pfmbackend.service.observer.Subscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class NotificationServiceImpl implements Subscriber, NotificationService {

    private final Logger LOGGER = LogManager.getLogger(LimitExpenseServiceImpl.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private LimitExpenseRepository limitExpenseRepository;

    @Autowired
    private ExpenseIncomeRepository expenseIncomeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private JwtAuthManager authManager;

    private final Map<PeriodType, String> messageFormatByPeriodType = Map.ofEntries(
        new AbstractMap.SimpleEntry<PeriodType, String>(PeriodType.DAY, "Bạn đã vượt quá giới hạn chi %d cho %s của ví %s một ngày"),
        new AbstractMap.SimpleEntry<PeriodType, String>(PeriodType.WEEK, "Bạn đã vượt quá giới hạn chi %d cho %s của ví %s một tuần"),
        new AbstractMap.SimpleEntry<PeriodType, String>(PeriodType.MONTH, "Bạn đã vượt quá giới hạn chi %d cho %s của ví %s một tháng"),
        new AbstractMap.SimpleEntry<PeriodType, String>(PeriodType.QUARTER, "Bạn đã vượt quá giới hạn chi %d cho %s của ví %s một quý"),
        new AbstractMap.SimpleEntry<PeriodType, String>(PeriodType.YEAR, "Bạn đã vượt quá giới hạn chi %d cho %s của ví %s một năm")
    );

    @Override
    public List<NotificationResponse> getAll() {
        try {
            User user = authManager.getUserByToken();
            List<Notification> notifications = notificationRepository.findAllByUserNo(user.getUserNo());
            return notifications.stream()
                    .map(noti -> NotificationResponse.builder()
                            .notificationNo(noti.getNotificationNo())
                            .message(noti.getMessage())
                            .isRead(noti.isRead())
                            .timeAgo(buildCreatedTimeAgoMessage(noti.getCreatedOn()))
                            .build())
                    .toList();
        } catch (Exception e) {
            LOGGER.error("Error when get all notifications");
        }
        return null;
    }

    @Override
    public void update(String userNo) {
        Optional<User> user = userRepository.findById(userNo);
        if (user.isEmpty()) {
            LOGGER.error(String.format("User no %s not exist", userNo));
            return;
        }

        List<LimitExpense> limitExpenses = limitExpenseRepository.findAllByUserNo(userNo);
        if (limitExpenses.isEmpty()) {
            LOGGER.info(String.format("User %s have no limit on expense", userNo));
            return;
        }

        List<Notification> newNotifications = new ArrayList<>();
        List<ExpenseIncome> expenses = expenseIncomeRepository.findAllByUserAndOperationType(user.get(),
                OperationType.EXPENSE);
        LOGGER.info("Got expense list from database");
        for (LimitExpense limitExpense : limitExpenses) {
            if ( !addByPeriodType(userNo, newNotifications, expenses, limitExpense, limitExpense.getPeriodType()) ) {
                return;
            }
        }
        LOGGER.info("Built new notification list");
        notificationRepository.saveAll(newNotifications);
        LOGGER.info("Saved new notifications");
    }

    private boolean addByPeriodType(String userNo, List<Notification> newNotifications, List<ExpenseIncome> expenses,
                                    LimitExpense limitExpense, PeriodType periodType) {
        if (limitExpense.getPeriodType() == periodType && isValidTimeLimit(limitExpense)) {
            String categoryNo = limitExpense.getCategoryNo();
            Optional<Category> categoryOpt = categoryRepository.findById(categoryNo);
            if (categoryOpt.isEmpty()) {
                LOGGER.error(String.format("Can not find category with id %s", categoryNo));
                return false;
            }
            long totalAmountOfDay = expenses.stream()
                    .filter(e -> e.getCategoryNo().equals(categoryNo) && e.getWallet().getWalletNo().equals(limitExpense.getWalletNo()))
                    .map(ExpenseIncome::getAmount)
                    .reduce(0L, Long::sum);
            if (totalAmountOfDay > limitExpense.getAmount()) {
                newNotifications.add(Notification.builder()
                        .createdOn(LocalDateTime.now())
                        .userNo(userNo)
                        .message(String.format(messageFormatByPeriodType.get(periodType),
                                limitExpense.getAmount(),
                                categoryOpt.get().getName(),
                                walletRepository.findById(limitExpense.getWalletNo()).get().getName()))
                        .build());
            }
        }
        return true;
    }

    private boolean isValidTimeLimit(LimitExpense limitExpense) {
        Date now = new Date();
        return limitExpense.getStartDate().before(now) && limitExpense.getEndDate().after(now);
    }

    private String buildCreatedTimeAgoMessage(LocalDateTime createdTime) {
        LocalDateTime now = LocalDateTime.now();

        long days = ChronoUnit.DAYS.between(createdTime, now);
        if (days > 0) {
            return String.format("%d ngày trước", days);
        }

        long hours = ChronoUnit.HOURS.between(createdTime, now);
        if (hours > 0) {
            return String.format("%d giờ trước", hours);
        }

        long minutes = ChronoUnit.MINUTES.between(createdTime, now);
        if (minutes > 0) {
            return String.format("%d phút trước", minutes);
        }

        return "Vừa xong";
    }
}
