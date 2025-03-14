package ru.effectivemobile.taskservice.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        UUID authorId,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
