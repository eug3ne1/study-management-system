package org.study.system.deepdivestudy.dto.auth;

import lombok.Data;
import org.study.system.deepdivestudy.entity.users.RoleName;

@Data
public class SignupRequest {

    private String email;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private RoleName roleName;

}
