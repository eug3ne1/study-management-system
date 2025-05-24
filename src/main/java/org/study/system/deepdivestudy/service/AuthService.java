package org.study.system.deepdivestudy.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.study.system.deepdivestudy.config.JwtUtils;
import org.study.system.deepdivestudy.dto.auth.AuthResponse;
import org.study.system.deepdivestudy.dto.auth.LoginRequest;
import org.study.system.deepdivestudy.dto.auth.SignupRequest;
import org.study.system.deepdivestudy.exceptions.EmailAlreadyExistsException;
import org.study.system.deepdivestudy.entity.users.*;
import org.study.system.deepdivestudy.repository.*;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetails customUserDetails;
    private final JwtUtils jwtUtils;


    @Transactional
    public AuthResponse register(SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Email already exists with another account");
        }
        User user = new User();
        user.setEmail(signupRequest.getEmail());

        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Role role = signupRequest.getRoleName() != null ?
                roleRepository.findByName(signupRequest.getRoleName()) :
                roleRepository.findByName(RoleName.ROLE_STUDENT);

        user.setRole(role);
        User savedUser = userRepository.save(user);

        createEntityByRole(savedUser,role,signupRequest);

        return generateAuthResponse(savedUser.getEmail(), role.getName().name());
    }

    private void createEntityByRole(User savedUser, Role role, SignupRequest signupRequest){
        if (role.getName() == RoleName.ROLE_TEACHER) {
            Teacher teacher = new Teacher();
            teacher.setUser(savedUser);
            teacher.setFirstName(signupRequest.getFirstName());
            teacher.setLastName(signupRequest.getLastName());
            teacher.setMiddleName(signupRequest.getMiddleName());
            teacherRepository.save(teacher);
        } else if (role.getName() == RoleName.ROLE_STUDENT) {
            Student student = new Student();
            student.setUser(savedUser);
            student.setFirstName(signupRequest.getFirstName());
            student.setLastName(signupRequest.getLastName());
            student.setMiddleName(signupRequest.getMiddleName());
            studentRepository.save(student);
        }
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String jwt = jwtUtils.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login Successfully");
        authResponse.setAuthorities(authorities);

        return authResponse;
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private AuthResponse generateAuthResponse(String email, String roleName) {
        var authorities = List.of((GrantedAuthority) () -> roleName);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("User created successfully");
        authResponse.setAuthorities(List.of(roleName));

        return authResponse;
    }

    public Authentication buildAuthentication(String email, String role){
        var authorities = List.of((GrantedAuthority) () -> role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
        return authentication;
    }


    @Transactional
    public User oAuth2SignUp(String email, String roleName){
        User user = new User();
        user.setEmail(email);

        if(roleName.equals("ROLE_STUDENT")){
            Role role = roleRepository.findByName(RoleName.ROLE_STUDENT);
            user.setRole(role);
            User savedUser = userRepository.save(user);
            Student student = new Student();
            student.setUser(savedUser);
            studentRepository.save(student);
        }else if (roleName.equals("ROLE_TEACHER")) {
            Role role = roleRepository.findByName(RoleName.ROLE_TEACHER);
            user.setRole(role);
            User savedUser = userRepository.save(user);
            Teacher teacher = new Teacher();
            teacher.setUser(savedUser);
            teacherRepository.save(teacher);
        }
        return user;
    }


    public String getRoleByEmail(String email){

        try {
            User user = userRepository.findByEmail(email);
            return user.getRole().getName().name();
        }catch (Exception e){
            return null;

        }


    }
}

