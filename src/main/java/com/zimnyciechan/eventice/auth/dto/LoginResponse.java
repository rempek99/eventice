package com.zimnyciechan.eventice.auth.dto;

import com.zimnyciechan.eventice.core.dto.ResponseObject;

import lombok.Getter;

@Getter
public class LoginResponse extends ResponseObject {

    private final String token;
    private final static String SUCCESS = "Logged Succesfully.";
    private final static String FAIL = "Login failed.";

    public LoginResponse(String token) {
        super(getMessage(token));
        this.token = token;
    }

    private static String getMessage(String token) {
        if (token != null) {
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

}
