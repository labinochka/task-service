package ru.effectivemobile.taskservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskservice.api.AuthApi;
import ru.effectivemobile.taskservice.dto.request.AuthRequest;
import ru.effectivemobile.taskservice.dto.response.AuthResponse;
import ru.effectivemobile.taskservice.dto.response.TokensResponse;
import ru.effectivemobile.taskservice.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public AuthResponse register(AuthRequest request) {
        return authService.register(request);
    }

    @Override
    public TokensResponse signIn(AuthRequest request) {
        return authService.signIn(request);
    }

    @Override
    public TokensResponse refreshTokens(String refreshToken) {
        return authService.refreshTokens(refreshToken);
    }
}
