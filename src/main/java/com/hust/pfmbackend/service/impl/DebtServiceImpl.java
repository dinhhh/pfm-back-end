package com.hust.pfmbackend.service.impl;

import com.hust.pfmbackend.entity.Debt;
import com.hust.pfmbackend.entity.OperationType;
import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.entity.UserDebtInfo;
import com.hust.pfmbackend.model.request.DebtRequest;
import com.hust.pfmbackend.model.response.statistic.DebtResponse;
import com.hust.pfmbackend.repository.DebtRepository;
import com.hust.pfmbackend.repository.UserDebtInfoRepository;
import com.hust.pfmbackend.security.jwt.JwtAuthManager;
import com.hust.pfmbackend.service.DebtService;
import com.hust.pfmbackend.service.UserDebtInfoService;
import com.hust.pfmbackend.utils.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DebtServiceImpl implements DebtService {

    private final Logger LOGGER = LogManager.getLogger(DebtServiceImpl.class);

    @Autowired
    private JwtAuthManager authManager;

    @Autowired
    private UserDebtInfoRepository userDebtInfoRepository;

    @Autowired
    private DebtRepository debtRepository;

    @Override
    public boolean save(DebtRequest request) {
        if (!Validator.isValidObject(request)) {
            LOGGER.error(String.format("Invalid request %s", request));
            return false;
        }

        try {
            User user = authManager.getUserByToken();
            LOGGER.info(String.format("Start saving new debt request: %s", request));

            if (request.getIsNewUserDebtInfo() == 1) {
                LOGGER.info("Try to create new user debt information");
                if (request.getUserDebtInfoName() == null) {
                    LOGGER.error("Try to create new user debt info with name is null");
                    return false;
                }
                userDebtInfoRepository.save(UserDebtInfo.builder()
                        .name(request.getUserDebtInfoName())
                        .userNo(user.getUserNo())
                        .build());
                LOGGER.info("Created new user debt information");
            }

            UserDebtInfo info = userDebtInfoRepository.findAllByUserNo(user.getUserNo())
                    .stream()
                    .filter(i -> i.getName().equals(request.getUserDebtInfoName()))
                    .findAny().orElse(null);
            if (info == null) {
                LOGGER.info("Select exist user debt info to new record");
            }

            Debt debt = Debt.builder()
                    .amount(request.getAmount())
                    .createdOn(request.getCreatedDate())
                    .description(request.getDescription())
                    .userDebtInfoNo(request.getIsNewUserDebtInfo() == 1 ? info.getUserDebtInfoNo() : request.getUserDebtInfoNo())
                    .operationType(OperationType.findByCode(request.getOperationCode()))
                    .expectCollectDate(request.getExpectCollectDate())
                    .userNo(user.getUserNo())
                    .build();
            LOGGER.info(String.format("Built new entity %s", debt));
            debtRepository.save(debt);
            LOGGER.info("Saved new debt entity");
            return true;
        } catch (Exception e) {
            LOGGER.error("Error when save new debt");
        }
        return false;
    }

    @Override
    public DebtResponse getAllBorrow() {
        return getDebtResponseByOperationType(OperationType.BORROW);
    }

    @Override
    public DebtResponse getAllLend() {
        return getDebtResponseByOperationType(OperationType.LEND);
    }

    @Override
    public boolean deleteByUserDebtInfoNo(String userDebtInfoNo) {
        try {
            User user = authManager.getUserByToken();
            List<Debt> debts = debtRepository.findAllByUserNoAndUserDebtInfoNo(user.getUserNo(), userDebtInfoNo);
            for (Debt debt : debts) {
                debt.setActualCollectDate(new Date());
            }
            debtRepository.saveAll(debts);
            LOGGER.info(String.format("Updated %d actual collect dates of all debt from user debt info no %s",
                    debts.size(),
                    userDebtInfoNo));
            return true;
        } catch (Exception e) {
            LOGGER.error(String.format("Error when delete by user debt info no %s", userDebtInfoNo));
        }
        return false;
    }

    private DebtResponse getDebtResponseByOperationType(OperationType ot) {
        try {
            User user = authManager.getUserByToken();
            List<Debt> debts = debtRepository.findAllByUserNoAndOperationType(user.getUserNo(), ot);
            Map<String, Long> debtUserInfoMap = new HashMap<>();
            long totalDebt = 0L;
            long returnedDebt = 0L;
            for (Debt debt : debts) {
                if (debt.getActualCollectDate() != null) {
                    continue;
                }

                String debtUserInfoNo = debt.getUserDebtInfoNo();
                if (debtUserInfoMap.containsKey(debtUserInfoNo)) {
                    long prevAmount = debtUserInfoMap.get(debtUserInfoNo);
                    debtUserInfoMap.put(debtUserInfoNo, prevAmount + debt.getAmount());
                } else {
                    debtUserInfoMap.put(debtUserInfoNo, debt.getAmount());
                }

                totalDebt += debt.getAmount();
                if (debt.getActualCollectDate() != null) {
                    returnedDebt += debt.getAmount();
                }
            }

            List<DebtResponse.UserDebtInfo> info = new ArrayList<>();
            for (Map.Entry<String, Long> entry : debtUserInfoMap.entrySet()) {
                String debtUserInfoNo = entry.getKey();
                Optional<UserDebtInfo> userDebtInfoOpt = userDebtInfoRepository.findById(debtUserInfoNo);
                if (userDebtInfoOpt.isEmpty()) {
                    LOGGER.warn(String.format("Something went wrong when get all user debt info with id %s",
                            debtUserInfoNo));
                    return null;
                }
                UserDebtInfo userDebtInfo = userDebtInfoOpt.get();
                info.add(DebtResponse.UserDebtInfo.builder()
                        .type(ot)
                        .userName(userDebtInfo.getName())
                        .amount(entry.getValue())
                        .userDebtInfoNo(userDebtInfo.getUserDebtInfoNo())
                        .build());
            }

            return DebtResponse.builder()
                    .totalDebt(totalDebt)
                    .returnedDebt(returnedDebt)
                    .remainingDebt(totalDebt - returnedDebt)
                    .infoList(info)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error when get all borrow");
        }
        return null;
    }

}
