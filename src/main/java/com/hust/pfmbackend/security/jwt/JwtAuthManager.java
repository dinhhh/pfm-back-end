package com.hust.pfmbackend.security.jwt;

import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthManager implements AuthenticationManager {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal(); // TODO: Expect
        String pw = (String) authentication.getCredentials(); // TODO: Expect
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with email %s not found", email));
        }

        if (pw.equals(user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(email, pw);
        }

        throw new BadCredentialsException("Password not match");
    }
}
