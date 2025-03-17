package ru.effectivemobile.taskservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(

        @NotBlank(message = "Комментарий не может быть пустым")
        String comment
) {
}
