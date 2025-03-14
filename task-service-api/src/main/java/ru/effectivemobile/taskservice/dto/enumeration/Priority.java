package ru.effectivemobile.taskservice.dto.enumeration;

import lombok.Getter;

@Getter
public enum Priority {
    LOW("������"),
    MEDIUM("�������"),
    HIGH("�������");

    private final String value;

    Priority(String value) {
        this.value = value;
    }
}
