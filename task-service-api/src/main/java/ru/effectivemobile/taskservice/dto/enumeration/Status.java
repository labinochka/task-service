package ru.effectivemobile.taskservice.dto.enumeration;

import lombok.Getter;

@Getter
public enum Status {
    FOR_EXECUTION("К выполнению"),
    IN_DEVELOPMENT("В разработке"),
    REVIEW("Ревью"),
    READY_FOR_TESTING("Готово к тестированию"),
    IN_TESTING("В тестировании"),
    DONE("Готово");

    private final String value;

    Status(String value) {
        this.value = value;
    }
}
