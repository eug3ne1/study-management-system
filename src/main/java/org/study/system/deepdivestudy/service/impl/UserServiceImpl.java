package org.study.system.deepdivestudy.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.study.system.deepdivestudy.config.JwtUtils;
import org.study.system.deepdivestudy.entity.users.User;
import org.study.system.deepdivestudy.repository.UserRepository;
import org.study.system.deepdivestudy.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    public User getUserByJWT(String jwt) {
        String email = jwtUtils.getEmailFromToken(jwt);
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }
}
