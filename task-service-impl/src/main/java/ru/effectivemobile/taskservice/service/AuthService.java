package ru.effectivemobile.taskservice.service;

import ru.effectivemobile.taskservice.dto.request.AuthRequest;
import ru.effectivemobile.taskservice.dto.response.AuthResponse;
import ru.effectivemobile.taskservice.dto.response.TokensResponse;

public interface AuthService {

    TokensResponse signIn(AuthRequest request);

    TokensResponse refreshTokens(String refreshToken);

    AuthResponse register(AuthRequest request);
}
