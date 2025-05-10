package org.study.system.deepdivestudy.config;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Data
public class JwtConstant {

    @Value("${jwt.secret}") public String SECRETE_KEY;

    public final String JWT_HEADER = "Authorization";
}
