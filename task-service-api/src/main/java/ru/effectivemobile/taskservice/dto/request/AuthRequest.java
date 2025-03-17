package ru.effectivemobile.taskservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(

        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Не соответствует формату email")
        String email,

        @NotBlank(message = "Пароль не может быть пустым")
        String password
) {
}
