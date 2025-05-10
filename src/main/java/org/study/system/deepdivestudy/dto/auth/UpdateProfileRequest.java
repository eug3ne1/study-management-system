package org.study.system.deepdivestudy.dto.auth;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String middleName;

    private String currentPassword;

    private String newPassword;
}