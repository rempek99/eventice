package com.zimnyciechan.eventice.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotNull
    @Size(min = 3, max = 16)
    private String username;
    
    @NotNull
    @Email
    private String email;
    
    @NotNull
    @Size(min = 6, max = 32)
    private String password;
}
