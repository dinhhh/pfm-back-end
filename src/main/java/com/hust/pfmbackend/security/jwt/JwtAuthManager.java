package com.hust.pfmbackend.security.jwt;

import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.repository.UserRepository;
import com.hust.pfmbackend.security.jwt.service.JwtUserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthManager implements AuthenticationManager {

    private final Logger LOGGER = LogManager.getLogger(JwtAuthManager.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String pw = (String) authentication.getCredentials();
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with email %s not found", email));
        }

        if (encoder.matches(pw, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(email, pw);
        }

        throw new BadCredentialsException("Password not match");
    }

    public String getUserNameByToken() throws Exception {
        try {
            LOGGER.info("Try to get user detail by token");
            JwtUserDetails details = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return details.getUsername();
        } catch (NullPointerException e) {
            throw new Exception("Request is not authed");
        }
    }

    public User getUserByToken() throws Exception {
        try {
            return userRepository.findUserByEmail(getUserNameByToken());
        } catch (Exception e) {
            throw new Exception("Request is not authed");
        }
    }
}
