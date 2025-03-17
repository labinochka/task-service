package ru.effectivemobile.taskservice.dto.response;

import org.springframework.data.domain.Page;
import ru.effectivemobile.taskservice.dto.enumeration.Priority;
import ru.effectivemobile.taskservice.dto.enumeration.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShortTaskResponse(
        UUID id,
        String title,
        Status status,
        Priority priority,
        UUID authorId,
        UUID executorId,
        LocalDateTime createdAt
) {
}
