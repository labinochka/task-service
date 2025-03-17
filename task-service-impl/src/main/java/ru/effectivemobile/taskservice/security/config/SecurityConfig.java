package ru.effectivemobile.taskservice.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.effectivemobile.taskservice.security.provider.AccessTokenFilter;
import ru.effectivemobile.taskservice.security.provider.HttpAuthenticationEntryPoint;
import ru.effectivemobile.taskservice.security.provider.JwtAuthenticationProvider;
import ru.effectivemobile.taskservice.security.util.JwtTokenUtils;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final HttpAuthenticationEntryPoint httpAuthenticationEntryPoint;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtTokenUtils jwtTokenUtils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/v1/task-service/auth/**", "/v3/api-docs/**", "/swagger-ui/**")
                        .permitAll().anyRequest().authenticated())
                .sessionManagement(AbstractHttpConfigurer::disable)
                .addFilterBefore(new AccessTokenFilter(jwtTokenUtils, authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e.authenticationEntryPoint(httpAuthenticationEntryPoint));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(jwtAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

