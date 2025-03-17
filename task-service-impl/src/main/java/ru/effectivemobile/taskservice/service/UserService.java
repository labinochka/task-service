package ru.effectivemobile.taskservice.service;

import ru.effectivemobile.taskservice.security.userdetails.CustomUserDetails;

import java.util.UUID;

public interface UserService {

    CustomUserDetails getCurrentUser();

    UUID getCurrentUserId();
}
