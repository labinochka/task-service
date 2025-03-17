package ru.effectivemobile.taskservice.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.effectivemobile.taskservice.dto.response.TokensResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    @Value("${jwt.expired.access-time}")
    private Long accessTime;

    @Value("${jwt.expired.refresh-time}")
    private Long refreshTime;

    private final Algorithm algorithm;

    public DecodedJWT decodeToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token.length() > 7 && token.startsWith("Bearer ") ? token.substring(7) : token);
    }

    public boolean verifyToken(String token) {
        try {
            return token != null && decodeToken(token).getExpiresAt().after(new Date());
        } catch (JWTVerificationException e) {
            log.warn("Invalid token {}", token);
            return false;
        } catch (Exception e) {
            log.warn("JWT decode error", e);
            return false;
        }
    }

    public TokensResponse createTokens(String id, String role) {
        return TokensResponse.builder()
                .accessToken(generateAccessToken(id, role))
                .refreshToken(generateRefreshToken(id))
                .refreshTokenExpiresAt(LocalDateTime.ofInstant(
                        new Date(System.currentTimeMillis() + refreshTime).toInstant(), ZoneId.systemDefault()))
                .build();
    }

    private String generateAccessToken(String id, String role) {
        return JWT.create()
                .withClaim("id", id)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTime))
                .sign(algorithm);
    }

    private String generateRefreshToken(String id) {
        return JWT.create()
                .withClaim("id", id)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTime))
                .sign(algorithm);
    }
}
