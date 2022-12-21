package com.hust.pfmbackend.service;

import com.hust.pfmbackend.model.request.SignInRequest;

public interface UserService {

    boolean signIn(SignInRequest request);

}
