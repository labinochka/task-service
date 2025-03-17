package ru.effectivemobile.taskservice.security.provider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.effectivemobile.taskservice.dto.enumeration.Role;
import ru.effectivemobile.taskservice.security.util.JwtTokenUtils;

import java.io.IOException;

@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null && jwtTokenUtils.verifyToken(token)) {
            String role = jwtTokenUtils.decodeToken(token).getClaim("role").asString();
            if (Role.contains(role)) {
                JwtAuthentication tokenAuthentication = new JwtAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);
                authenticationManager.authenticate(tokenAuthentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        } else {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
