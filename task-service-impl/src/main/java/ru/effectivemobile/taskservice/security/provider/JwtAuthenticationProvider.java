package ru.effectivemobile.taskservice.security.provider;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.effectivemobile.taskservice.dto.enumeration.Role;
import ru.effectivemobile.taskservice.security.userdetails.CustomUserDetails;
import ru.effectivemobile.taskservice.security.util.JwtTokenUtils;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getName();
        DecodedJWT decodedJWT = jwtTokenUtils.decodeToken(token);
        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;
        if (decodedJWT != null) {
            String role = decodedJWT.getClaim("role").as(String.class);
            UUID id = decodedJWT.getClaim("id").as(UUID.class);
            jwtAuthentication.setAuthenticated(true);
            jwtAuthentication.setUserDetails(new CustomUserDetails(id, Role.valueOf(role)));
        } else {
            jwtAuthentication.setAuthenticated(false);
        }
        return jwtAuthentication;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.equals(authentication);
    }
}
