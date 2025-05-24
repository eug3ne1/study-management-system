package org.study.system.deepdivestudy.service;

import jakarta.transaction.Transactional;
import org.study.system.deepdivestudy.dto.auth.AuthResponse;
import org.study.system.deepdivestudy.dto.auth.LoginRequest;
import org.study.system.deepdivestudy.dto.auth.SignupRequest;

public interface AuthService {


    AuthResponse signup(SignupRequest signupRequest);

    AuthResponse login(LoginRequest loginRequest);

    String oAuth2SignUp(String email, String role);



}
