package ru.effectivemobile.taskservice.dto.enumeration;

import lombok.Getter;

@Getter
public enum Priority {
    LOW("Низкий"),
    MEDIUM("Средний"),
    HIGH("Высокий");

    private final String value;

    Priority(String value) {
        this.value = value;
    }
}
