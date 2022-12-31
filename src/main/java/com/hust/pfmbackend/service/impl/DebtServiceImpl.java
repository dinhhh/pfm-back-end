package com.hust.pfmbackend.service.impl;

import com.hust.pfmbackend.entity.Debt;
import com.hust.pfmbackend.entity.OperationType;
import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.entity.UserDebtInfo;
import com.hust.pfmbackend.model.request.DebtRequest;
import com.hust.pfmbackend.model.request.UserDebtInfoRequest;
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

@Service
public class DebtServiceImpl implements DebtService {

    private final Logger LOGGER = LogManager.getLogger(DebtServiceImpl.class);

    @Autowired
    private JwtAuthManager authManager;

    @Autowired
    private UserDebtInfoRepository userDebtInfoRepository;

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private UserDebtInfoService userDebtInfoService;

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
                LOGGER.error("Error when get new user debt info");
                return false;
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
}
