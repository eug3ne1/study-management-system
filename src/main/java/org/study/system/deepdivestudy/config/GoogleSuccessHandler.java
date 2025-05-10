package org.study.system.deepdivestudy.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.study.system.deepdivestudy.model.users.Role;
import org.study.system.deepdivestudy.service.AuthService;
import org.study.system.deepdivestudy.service.UserService;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class GoogleSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final JwtUtils jwtUtils;


    private final UserService userService;   // шукаємо/створюємо юзера, визначаємо ролі
    @Value("${frontend.redirect}") String redirect;

    @Value("${frontend.role.select.page}") String roleSelectPage;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse resp,
                                        Authentication authentication) throws IOException {
        OAuth2User oUser = (OAuth2User) authentication.getPrincipal();
        String email = oUser.getAttribute("email");

        String role = authService.getRoleByEmail(email);
        if (role == null) {
            // ще не обрав роль — відправляємо його на фронтенд-сторінку вибору ролі
            resp.sendRedirect(roleSelectPage + "?email=" + URLEncoder.encode(email, UTF_8));
            return;
        }

        var authorities = List.of((GrantedAuthority) () -> role);

        // ⸺⸺⸺ підтягуємо/створюємо юзера та Spring-Auth ⸺⸺⸺

        Authentication auth = new UsernamePasswordAuthenticationToken(email,null,authorities);

        String jwt = jwtUtils.generateToken(auth);

        // фронтенд очікує ?token=
        resp.sendRedirect(redirect + "?token=" + jwt);
    }
}
