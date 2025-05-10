package org.study.system.deepdivestudy.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.study.system.deepdivestudy.config.JwtUtils;
import org.study.system.deepdivestudy.dto.auth.AuthResponse;
import org.study.system.deepdivestudy.dto.auth.LoginRequest;
import org.study.system.deepdivestudy.dto.auth.RoleSelectRequest;
import org.study.system.deepdivestudy.dto.auth.SignupRequest;
import org.study.system.deepdivestudy.model.users.User;
import org.study.system.deepdivestudy.service.AuthService;
import org.study.system.deepdivestudy.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    @Value("${frontend.redirect}") String redirect;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(@RequestBody SignupRequest signupRequest) {
        AuthResponse response = authService.register(signupRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(
            path = "/oauth2/complete",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public void completeOAuth2(
            @RequestParam("email") String email,
            @RequestParam("role") String role,
            HttpServletResponse resp
    ) throws IOException {

        Authentication auth = authService.buildAuthentication(email, role);
        authService.oAuth2SignUp(email,role);
        String token = jwtUtils.generateToken(auth);

        resp.sendRedirect(redirect + "?token=" + token);
    }
}
