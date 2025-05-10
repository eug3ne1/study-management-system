package org.study.system.deepdivestudy.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class RoleCheckController {

    @GetMapping("/check-roles")
    public String checkRoles(Authentication authentication) {

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        return "Ролі користувача: " + roles;
    }
}