package org.study.system.deepdivestudy.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.study.system.deepdivestudy.entity.users.Role;
import org.study.system.deepdivestudy.entity.users.User;
import org.study.system.deepdivestudy.service.RoleService;
import org.study.system.deepdivestudy.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final UserService userService;

    @Override
    public Optional<Role> getRoleByEmail(String email) {
        try {
            User user = userService.findByEmail(email);
            return Optional.ofNullable(user.getRole());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

