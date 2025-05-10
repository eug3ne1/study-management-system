package org.study.system.deepdivestudy.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
}

