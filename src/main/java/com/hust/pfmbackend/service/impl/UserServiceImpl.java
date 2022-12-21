package com.hust.pfmbackend.service.impl;

import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.model.request.SignInRequest;
import com.hust.pfmbackend.repository.UserRepository;
import com.hust.pfmbackend.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean signIn(SignInRequest request) {
        String email = request.getEmail();
        String pw = request.getPw();
        String fullName = request.getFullName();
        if (email == null || pw == null || fullName == null) {
            logger.info(String.format("SignInRequest have null field: %s", request));
            return false;
        }

        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            logger.info("User not exist in db");
            String encodedPw = passwordEncoder.encode(pw);
            userRepository.save(User.builder()
                    .email(email)
                    .password(encodedPw)
                    .fullName(fullName)
                    .build());
            logger.info("Saved new user");
            return true;
        }
        return false;
    }

}
