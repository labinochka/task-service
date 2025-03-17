package ru.effectivemobile.taskservice.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskservice.security.userdetails.CustomUserDetails;
import ru.effectivemobile.taskservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public CustomUserDetails getCurrentUser() {
        return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails());
    }
}
