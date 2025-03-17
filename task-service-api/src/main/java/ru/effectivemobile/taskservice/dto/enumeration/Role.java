package ru.effectivemobile.taskservice.dto.enumeration;

import java.util.Arrays;

public enum Role {
    USER,
    ADMIN;

    public static boolean contains(String role) {
        return Arrays.stream(Role.values())
                .anyMatch(userRole -> userRole.name().equals(role));
    }
}
