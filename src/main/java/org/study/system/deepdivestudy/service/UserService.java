package org.study.system.deepdivestudy.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.study.system.deepdivestudy.config.JwtUtils;
import org.study.system.deepdivestudy.entity.users.User;
import org.study.system.deepdivestudy.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;



    public User getUserByJWT(String jwt){
        String email = jwtUtils.getEmailFromToken(jwt);
        return userRepository.findByEmail(email);
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
