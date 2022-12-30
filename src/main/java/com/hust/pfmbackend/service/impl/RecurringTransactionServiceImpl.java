package com.hust.pfmbackend.service.impl;

import com.hust.pfmbackend.constant.DatePattern;
import com.hust.pfmbackend.entity.OperationType;
import com.hust.pfmbackend.entity.PeriodType;
import com.hust.pfmbackend.entity.RecurringTransaction;
import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.model.request.EditRecurringTransactionRequest;
import com.hust.pfmbackend.model.request.NewRecurringTransactionRequest;
import com.hust.pfmbackend.model.response.RecurringTransactionResponse;
import com.hust.pfmbackend.repository.RecurringTransactionRepository;
import com.hust.pfmbackend.security.jwt.JwtAuthManager;
import com.hust.pfmbackend.service.RecurringTransactionService;
import com.hust.pfmbackend.utils.DateFormatUtils;
import com.hust.pfmbackend.utils.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecurringTransactionServiceImpl implements RecurringTransactionService {

    private final Logger LOGGER = LogManager.getLogger(RecurringTransactionServiceImpl.class);

    @Autowired
    private RecurringTransactionRepository recurringTransactionRepository;

    @Autowired
    private JwtAuthManager authManager;

    @Override
    public boolean save(NewRecurringTransactionRequest request) {

        if ( !Validator.isValidObject(request) ) {
            LOGGER.error(String.format("Invalid request %s", request));
            return false;
        }

        if (request.getStartDate().after(request.getEndDate())) {
            LOGGER.error(String.format("Start date %s is after end date %s",
                    request.getStartDate(), request.getEndDate()));
            return false;
        }

        try {
            User user = authManager.getUserByToken();
            RecurringTransaction transaction = RecurringTransaction.builder()
                    .userNo(user.getUserNo())
                    .amount(request.getAmount())
                    .description(request.getDescription())
                    .operationType(OperationType.findByCode(request.getOperationCode()))
                    .periodType(PeriodType.findByCode(request.getPeriodCode()))
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .name(request.getName())
                    .build();
            recurringTransactionRepository.save(transaction);
            LOGGER.info("Saved new recurring transaction");
            return true;
        } catch (Exception e) {
            LOGGER.error("Error when saving new recurring transaction");
        }

        return false;
    }

    @Override
    public List<RecurringTransactionResponse> getAll() {
        try {
            User user = authManager.getUserByToken();
            List<RecurringTransaction> transactions = recurringTransactionRepository.findAllByUserNo(user.getUserNo());
            if (transactions.isEmpty()) {
                LOGGER.info("Response is empty");
            }
            return transactions.stream()
                    .map(transaction -> RecurringTransactionResponse.builder()
                            .recurringTransactionNo(transaction.getRecurringTransactionNo())
                            .startDate(DateFormatUtils.convertToString(transaction.getStartDate(), DatePattern.DD_MM_YYYY))
                            .endDate(DateFormatUtils.convertToString(transaction.getEndDate(), DatePattern.DD_MM_YYYY))
                            .operationCode(transaction.getOperationType().getCode())
                            .periodCode(transaction.getPeriodType().getCode())
                            .description(transaction.getDescription())
                            .name(transaction.getName())
                            .amount(transaction.getAmount())
                            .build())
                    .toList();
        } catch (Exception e) {
            LOGGER.error("Error when get all recurring transaction");
        }
        return null;
    }

    @Override
    public boolean update(EditRecurringTransactionRequest request) {

        if ( !Validator.isValidObject(request) ) {
            LOGGER.error(String.format("Invalid request %s", request));
            return false;
        }

        try {
            User user = authManager.getUserByToken();
            Optional<RecurringTransaction> oldOpt = recurringTransactionRepository.findById(request.getRecurringTransactionNo());
            if (oldOpt.isEmpty()) {
                LOGGER.warn(String.format("This edit transaction is not exist in data base with id %s",
                        request.getRecurringTransactionNo()));
                return false;
            }

            RecurringTransaction old = oldOpt.get();
            if ( !user.getUserNo().equals(old.getUserNo()) ) {
                LOGGER.warn("User have not own this recurring transaction");
                return false;
            }

            old.setAmount(request.getAmount());
            old.setDescription(request.getDescription() != null ? request.getDescription() : old.getDescription());
            old.setName(request.getName());
            old.setStartDate(request.getStartDate() != null ? request.getStartDate() : old.getStartDate());
            old.setEndDate(request.getEndDate() != null ? request.getEndDate() : old.getEndDate());
            recurringTransactionRepository.save(old);
            LOGGER.info("Updated recurring transaction....");
            return true;
        } catch (Exception e) {
            LOGGER.error("Error when get all recurring transaction");
        }

        return false;
    }

    @Override
    public boolean delete(String recurringTransactionNo) {
        if (recurringTransactionNo != null) {
            recurringTransactionRepository.deleteById(recurringTransactionNo);
            return true;
        }
        return false;
    }


}
