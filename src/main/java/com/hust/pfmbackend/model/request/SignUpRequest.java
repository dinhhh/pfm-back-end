package com.hust.pfmbackend.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@ToString
public class SignUpRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String email;

    @NotBlank
    private String pw;

}
