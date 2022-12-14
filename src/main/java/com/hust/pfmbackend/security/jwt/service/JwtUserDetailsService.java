package com.hust.pfmbackend.security.jwt.service;

import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByEmail(username);
        if (user != null) {
            return new JwtUserDetails(user);
        } else {
            throw new UsernameNotFoundException(String.format("Not found user with name: %s", username));
        }
    }
}
