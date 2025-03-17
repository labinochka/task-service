package ru.effectivemobile.taskservice.dto.response;

import java.util.UUID;

public record AuthResponse(

        UUID id,

        String email
) {
}
