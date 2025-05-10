package org.study.system.deepdivestudy.dto.auth;

import lombok.Data;

import java.util.List;

@Data
public class AuthResponse {

    private String jwt;
    private String message;
    private List<String> authorities;
}
