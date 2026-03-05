package com.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String pseudo;
    private String password;
}
