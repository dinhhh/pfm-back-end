package com.hust.pfmbackend.service.impl;

import com.hust.pfmbackend.constant.DatePattern;
import com.hust.pfmbackend.entity.LimitExpense;
import com.hust.pfmbackend.entity.PeriodType;
import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.entity.Wallet;
import com.hust.pfmbackend.model.request.LimitExpenseRequest;
import com.hust.pfmbackend.model.response.LimitExpenseResponse;
import com.hust.pfmbackend.repository.CategoryRepository;
import com.hust.pfmbackend.repository.LimitExpenseRepository;
import com.hust.pfmbackend.repository.WalletRepository;
import com.hust.pfmbackend.security.jwt.JwtAuthManager;
import com.hust.pfmbackend.service.LimitExpenseService;
import com.hust.pfmbackend.utils.DateFormatUtils;
import com.hust.pfmbackend.utils.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LimitExpenseServiceImpl implements LimitExpenseService {

    private final Logger LOGGER = LogManager.getLogger(LimitExpenseServiceImpl.class);

    @Autowired
    private LimitExpenseRepository limitExpenseRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JwtAuthManager authManager;

    @Override
    public boolean save(LimitExpenseRequest request) {
        if ( !Validator.isValidObject(request) ) {
            LOGGER.error(String.format("Request %s is not valid", request));
            return false;
        }

        if (walletRepository.findById(request.getWalletNo()).isEmpty()) {
            LOGGER.error(String.format("Wallet no %s is not exist in database", request
                    .getWalletNo()));
            return false;
        }

        if (request.getCategoryNo() != null && categoryRepository.findById(request.getCategoryNo()).isEmpty()) {
            LOGGER.error(String.format("Category no %s is not exist in database", request.getCategoryNo()));
            return false;
        }

        try {
            User user = authManager.getUserByToken();
            LimitExpense le = LimitExpense.builder()
                    .userNo(user.getUserNo())
                    .amount(request.getAmount())
                    .periodType(PeriodType.findByCode(request.getPeriodTypeCode()))
                    .walletNo(request.getWalletNo())
                    .categoryNo(request.getCategoryNo())
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .name(request.getName())
                    .remainingAmount(request.getAmount())
                    .build();
            limitExpenseRepository.save(le);
            LOGGER.info("Saved new limit expense entity");
            return true;
        } catch (Exception e) {
            LOGGER.error("Error when save new limit expense");
        }

        return false;
    }

    @Override
    public List<LimitExpenseResponse> getAll() {
        try {
            User user = authManager.getUserByToken();
            List<LimitExpenseResponse> responses = new ArrayList<>();

            List<LimitExpense> limitExpenses = limitExpenseRepository.findAllByUserNo(user.getUserNo());
            if (limitExpenses.isEmpty()) {
                LOGGER.info("Limit expense is empty");
            }

            for (LimitExpense le : limitExpenses) {
                LocalDateTime startDate = convertToLocalDateTime(le.getStartDate());
                LocalDateTime endDate = convertToLocalDateTime(le.getEndDate());
                long dayLeft = Duration.between(startDate, endDate).toDays();
                responses.add(LimitExpenseResponse.builder()
                                .name(le.getName())
                                .dayLeft(dayLeft)
                                .remainingAmount(le.getRemainingAmount())
                                .amount(le.getAmount())
                                .walletName(walletRepository.findById(le.getWalletNo()).map(Wallet::getName)
                                        .orElseThrow())
                                .endDate(DateFormatUtils.convertToString(le.getStartDate(), DatePattern.DD_MM_YYYY))
                                .startDate(DateFormatUtils.convertToString(le.getEndDate(), DatePattern.DD_MM_YYYY))
                        .build());
            }
            return responses;
        } catch (Exception e) {
            LOGGER.error("Error when save new limit expense");
        }
        return null;
    }

    private LocalDateTime convertToLocalDateTime(Date d) {
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}