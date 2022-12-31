package com.hust.pfmbackend.service.impl;

import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.entity.UserDebtInfo;
import com.hust.pfmbackend.model.request.UserDebtInfoRequest;
import com.hust.pfmbackend.model.response.UserDebtInfoResponse;
import com.hust.pfmbackend.repository.UserDebtInfoRepository;
import com.hust.pfmbackend.security.jwt.JwtAuthManager;
import com.hust.pfmbackend.service.UserDebtInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDebtInfoServiceImpl implements UserDebtInfoService {

    private final Logger LOGGER = LogManager.getLogger(UserDebtInfoServiceImpl.class);

    @Autowired
    private JwtAuthManager authManager;

    @Autowired
    private UserDebtInfoRepository userDebtInfoRepository;

    @Override
    public boolean save(UserDebtInfoRequest request) {
        try {
            User user = authManager.getUserByToken();
            userDebtInfoRepository.save(UserDebtInfo.builder()
                    .name(request.getName())
                    .userNo(user.getUserNo())
                    .build());
            LOGGER.info("Saved new user debt information");
            return true;
        } catch (Exception e) {
            LOGGER.error("Error when save new user debt information");
        }
        return false;
    }

    @Override
    public List<UserDebtInfoResponse> getAll() {
        try {
            User user = authManager.getUserByToken();
            List<UserDebtInfo> response = userDebtInfoRepository.findAllByUserNo(user.getUserNo());
            LOGGER.info("Get all user debt information");
            return response.stream()
                    .map(r -> UserDebtInfoResponse.builder()
                            .name(r.getName())
                            .userDebtInfoNo(r.getUserDebtInfoNo())
                            .build())
                    .toList();
        } catch (Exception e) {
            LOGGER.error("Error when save new user debt information");
        }
        return null;
    }
}
