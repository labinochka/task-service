package ru.effectivemobile.taskservice.security.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret.key}")
    public String key;

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256(key);
    }

    @Bean
    @RequestScope
    public PreAuthenticatedAuthenticationToken jwt() {
        return new PreAuthenticatedAuthenticationToken(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }
}
