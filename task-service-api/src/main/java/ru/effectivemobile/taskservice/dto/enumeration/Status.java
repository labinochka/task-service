package ru.effectivemobile.taskservice.dto.enumeration;

import lombok.Getter;

@Getter
public enum Status {
    FOR_EXECUTION("� ����������"),
    IN_DEVELOPMENT("� ����������"),
    REVIEW("�����"),
    READY_FOR_TESTING("������ � ������������"),
    IN_TESTING("� ������������"),
    DONE("������");

    private final String value;

    Status(String value) {
        this.value = value;
    }
}
