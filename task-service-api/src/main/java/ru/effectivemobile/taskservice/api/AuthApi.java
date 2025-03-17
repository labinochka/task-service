package ru.effectivemobile.taskservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.effectivemobile.taskservice.dto.request.AuthRequest;
import ru.effectivemobile.taskservice.dto.response.AuthResponse;
import ru.effectivemobile.taskservice.dto.response.TokensResponse;

@Tag(name = "Authentication API | Аутентификация")
@Schema(description = "Работа с задачами")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
        @ApiResponse(responseCode = "404", description = "Запрашиваемый ресурс не найден"),
        @ApiResponse(responseCode = "500", description = "Ведутся технические работы")
})
@RequestMapping("/api/v1/task-service/auth")
public interface AuthApi {

    @Operation(summary = "Регистрация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Регистрация прошла успешно")
    })
    @PostMapping("/register")
    AuthResponse register(@Valid @RequestBody AuthRequest request);

    @Operation(summary = "Аутентификация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аутентификация прошла успешно")
    })
    @PostMapping("/sign-in")
    TokensResponse signIn(@Valid @RequestBody AuthRequest request);

    @Operation(summary = "Обновление токенов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токены обновлены")
    })
    @GetMapping("/refresh")
    TokensResponse refreshTokens(@RequestHeader("refresh-token") String refreshToken);
}
