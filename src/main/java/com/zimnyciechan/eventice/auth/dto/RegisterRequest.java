package com.zimnyciechan.eventice.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    private static final String PASSWORD_INVALID_MESSAGE = "Password: must contain from 6 to 32 characters, including: 1 lowercase letter, 1 capitalletter, 1 special character,1 digit";

    @NotNull
    @Size(min = 3, max = 16)
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6, max = 32, message = PASSWORD_INVALID_MESSAGE)
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,32}$", message = PASSWORD_INVALID_MESSAGE)
    private String password;
}
