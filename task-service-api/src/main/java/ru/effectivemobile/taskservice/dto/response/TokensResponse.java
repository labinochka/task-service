package ru.effectivemobile.taskservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TokensResponse(

    String accessToken,

    String refreshToken,

    @JsonIgnore
    LocalDateTime refreshTokenExpiresAt
) {
}
