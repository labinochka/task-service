package ru.effectivemobile.taskservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.effectivemobile.taskservice.dto.enumeration.Priority;
import ru.effectivemobile.taskservice.dto.enumeration.Status;

import java.util.UUID;

public record TaskRequest(

        @NotBlank(message = "Название не может быть пустым")
        @Size(min = 2, max = 100, message = "Название не может превышать 100 символов")
        String title,

        @NotBlank(message = "Описание не может быть пустым")
        String description,

        @NotNull(message = "Статус не может быть пустым")
        Status status,

        @NotNull(message = "Приоритет не может быть пустым")
        Priority priority,

        @NotNull(message = "Автор не может быть пустым")
        UUID authorId,

        @NotNull(message = "Исполнитель не может быть пустым")
        UUID executorId
) {
}
