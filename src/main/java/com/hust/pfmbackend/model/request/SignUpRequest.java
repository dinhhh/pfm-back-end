package com.hust.pfmbackend.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class SignUpRequest {

    private String fullName;
    private String email;
    private String pw;

}
