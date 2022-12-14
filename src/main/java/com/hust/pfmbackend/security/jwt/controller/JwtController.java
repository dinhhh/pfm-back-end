package com.hust.pfmbackend.security.jwt.controller;

import com.hust.pfmbackend.security.jwt.model.JwtRequest;
import com.hust.pfmbackend.security.jwt.model.JwtResponse;
import com.hust.pfmbackend.security.jwt.service.JwtUserDetailsService;
import com.hust.pfmbackend.security.jwt.service.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class JwtController {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenManager tokenManager;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> createToken(@RequestBody JwtRequest request) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("User disabled", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials", e);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String jwtToken = tokenManager.generateJwtToken(userDetails);
        return ResponseEntity.ok(JwtResponse.builder().token(jwtToken).build());
    }

}
