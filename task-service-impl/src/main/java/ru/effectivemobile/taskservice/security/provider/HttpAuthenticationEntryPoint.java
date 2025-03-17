package ru.effectivemobile.taskservice.security.provider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.effectivemobile.taskservice.security.util.JwtTokenUtils;

@Component
@RequiredArgsConstructor
public class HttpAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        if (request.getHeader(HttpHeaders.AUTHORIZATION) == null ||
                (request.getHeader(HttpHeaders.AUTHORIZATION) != null && !jwtTokenUtils.verifyToken(
                        request.getHeader(HttpHeaders.AUTHORIZATION)))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
