package org.study.system.deepdivestudy.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
@Component
@AllArgsConstructor
public class JwtUtils {

    public JwtConstant jwtConstant;


    public String generateToken(Authentication auth){
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        String authoritiesString = authorities.stream().map(GrantedAuthority::getAuthority)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
        SecretKey SECRETE_KEY = Keys.hmacShaKeyFor(jwtConstant.SECRETE_KEY.getBytes());
        return Jwts.builder().setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000)) // token valid for 1 day
                .claim("email", auth.getName())
                .claim("authorities", authoritiesString)
                .signWith(SECRETE_KEY)
                .compact();
    }

    public String getEmailFromToken(String jwt){
        jwt = jwt.substring(7);
        SecretKey SECRETE_KEY = Keys.hmacShaKeyFor(jwtConstant.SECRETE_KEY.getBytes());
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(SECRETE_KEY).build().parseClaimsJws(jwt).getBody();
            return String.valueOf(claims.get("email"));
        } catch (Exception e){
            throw new BadCredentialsException("invalid token");

        }
    }


}
