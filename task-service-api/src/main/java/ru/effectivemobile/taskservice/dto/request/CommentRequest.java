package ru.effectivemobile.taskservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CommentRequest(

        @NotNull(message = "Автор не может быть пустым")
        UUID authorId,

        @NotBlank(message = "Комментарий не может быть пустым")
        String comment
) {
}
