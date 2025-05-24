package org.study.system.deepdivestudy.service;

import org.study.system.deepdivestudy.entity.users.User;

import java.util.List;

public interface UserService {

     User getUserByJWT(String jwt);

     User findByEmail(String email);

     User save(User user);

     void deleteById(Long userId);

     List<User> getAll();



}
