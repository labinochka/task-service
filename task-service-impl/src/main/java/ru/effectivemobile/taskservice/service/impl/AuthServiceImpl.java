package ru.effectivemobile.taskservice.service.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectivemobile.taskservice.dto.enumeration.Role;
import ru.effectivemobile.taskservice.dto.request.AuthRequest;
import ru.effectivemobile.taskservice.dto.response.AuthResponse;
import ru.effectivemobile.taskservice.dto.response.TokensResponse;
import ru.effectivemobile.taskservice.entity.RefreshTokenEntity;
import ru.effectivemobile.taskservice.entity.UserEntity;
import ru.effectivemobile.taskservice.exception.model.MismatchedTokenException;
import ru.effectivemobile.taskservice.exception.model.UserAlreadyExistException;
import ru.effectivemobile.taskservice.exception.model.UserNotFoundException;
import ru.effectivemobile.taskservice.mapper.UserMapper;
import ru.effectivemobile.taskservice.repository.RefreshTokenRepository;
import ru.effectivemobile.taskservice.repository.UserRepository;
import ru.effectivemobile.taskservice.security.util.JwtTokenUtils;
import ru.effectivemobile.taskservice.service.AuthService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public TokensResponse signIn(AuthRequest request) {
        String email = request.email();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        checkPassword(request.password(), user.getPassword());
        TokensResponse tokens = jwtTokenUtils.createTokens(String.valueOf(user.getId()), user.getRole().name());
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .user(user)
                .refreshToken(tokens.refreshToken())
                .expiredAt(tokens.refreshTokenExpiresAt())
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
        return tokens;
    }

    @Override
    public TokensResponse refreshTokens(String refreshToken) {
        String id;
        try {
            id = jwtTokenUtils.decodeToken(refreshToken).getClaim("id").asString();
        } catch (JWTVerificationException e) {
            throw new MismatchedTokenException(e.getMessage());
        }
        UserEntity user = userRepository.findById(UUID.fromString(id)).orElseThrow(UserNotFoundException::new);
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new MismatchedTokenException("Неверный refresh токен"));
        TokensResponse tokens = jwtTokenUtils.createTokens(id, user.getRole().name());
        refreshTokenEntity.setRefreshToken(tokens.refreshToken());
        refreshTokenEntity.setExpiredAt(tokens.refreshTokenExpiresAt());
        refreshTokenRepository.save(refreshTokenEntity);
        return tokens;
    }

    @Override
    public AuthResponse register(AuthRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistException(request.email());
        }

        Role role = Role.USER;
        UserEntity user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Scheduled(cron = "${cron.refresh-token.clear}", zone = "Europe/Moscow")
    @Transactional
    public void removeExpiredTokens() {
        refreshTokenRepository.deleteByExpiredAtBefore(LocalDateTime.now());
    }

    private void checkPassword(final String presentedPassword, final String currentHashPassword) {
        if (!passwordEncoder.matches(presentedPassword, currentHashPassword)) {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
