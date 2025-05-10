package org.study.system.deepdivestudy.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.study.system.deepdivestudy.dto.auth.ProfileResponse;
import org.study.system.deepdivestudy.dto.auth.UpdateProfileRequest;
import org.study.system.deepdivestudy.model.users.*;
import org.study.system.deepdivestudy.service.CustomUserDetails;
import org.study.system.deepdivestudy.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("api/profile")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<ProfileResponse> getUser(@RequestHeader("Authorization") String jwt){
        User user = userService.getUserByJWT(jwt);
        ProfileResponse profileResponse = new ProfileResponse();
        Role role = user.getRole();
        if (role.getName() == RoleName.ROLE_TEACHER) {
            profileResponse.setFirstName(user.getTeacher().getFirstName());
            profileResponse.setLastName(user.getTeacher().getLastName());
            profileResponse.setMiddleName(user.getTeacher().getMiddleName());
        } else if (role.getName() == RoleName.ROLE_STUDENT) {
            profileResponse.setFirstName(user.getStudent().getFirstName());
            profileResponse.setLastName(user.getStudent().getLastName());
            profileResponse.setMiddleName(user.getStudent().getMiddleName());

        }
        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            Principal principal,
            @Valid @RequestBody UpdateProfileRequest req
    ) {
        User user = userService.findByEmail(principal.getName());
        Role role = user.getRole();
        if (role.getName() == RoleName.ROLE_TEACHER) {
           user.getTeacher().setFirstName(req.getFirstName());
           user.getTeacher().setLastName(req.getLastName());
           user.getTeacher().setMiddleName(req.getMiddleName());
        } else if (role.getName() == RoleName.ROLE_STUDENT) {
            user.getStudent().setFirstName(req.getFirstName());
            user.getStudent().setLastName(req.getLastName());
            user.getStudent().setMiddleName(req.getMiddleName());
        }
        if (req.getCurrentPassword() != null && req.getNewPassword() != null) {
            if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
                throw new BadCredentialsException("Невірний поточний пароль");
            }
            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        }

        userService.save(user);

        ProfileResponse resp = new ProfileResponse(
                req.getFirstName(),
                req.getLastName(),
                req.getMiddleName(),
                user.getEmail()
        );
        return ResponseEntity.ok(resp);
    }

}
