package org.study.system.deepdivestudy.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.study.system.deepdivestudy.entity.users.Role;
import org.study.system.deepdivestudy.service.RoleService;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class GoogleSuccessHandler implements AuthenticationSuccessHandler {

    private final RoleService roleService;
    private final JwtUtils jwtUtils;

    @Value("${frontend.redirect}")
    String redirect;

    @Value("${frontend.role.select.page}")
    String roleSelectPage;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse resp,
                                        Authentication authentication) throws IOException {
        OAuth2User oUser = (OAuth2User) authentication.getPrincipal();
        String email = oUser.getAttribute("email");

        Optional<Role> roleByEmail = roleService.getRoleByEmail(email);
        if (roleByEmail.isEmpty()) {
            resp.sendRedirect(roleSelectPage + "?email=" + URLEncoder.encode(email, UTF_8));
            return;
        }
        var authorities = List.of((GrantedAuthority) () -> roleByEmail.get().getName().name());
        Authentication auth = new UsernamePasswordAuthenticationToken(email,null,authorities);
        String jwt = jwtUtils.generateToken(auth);

        resp.sendRedirect(redirect + "?token=" + jwt);
    }
}
