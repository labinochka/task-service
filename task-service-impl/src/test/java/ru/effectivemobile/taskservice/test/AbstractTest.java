package ru.effectivemobile.taskservice.test;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.effectivemobile.taskservice.dto.enumeration.Priority;
import ru.effectivemobile.taskservice.dto.enumeration.Role;
import ru.effectivemobile.taskservice.dto.enumeration.Status;
import ru.effectivemobile.taskservice.entity.CommentEntity;
import ru.effectivemobile.taskservice.entity.TaskEntity;
import ru.effectivemobile.taskservice.entity.UserEntity;
import ru.effectivemobile.taskservice.initializer.PostgresInitializer;
import ru.effectivemobile.taskservice.repository.UserRepository;
import ru.effectivemobile.taskservice.security.provider.JwtAuthentication;
import ru.effectivemobile.taskservice.security.userdetails.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgresInitializer.class})
public class AbstractTest {

    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        UUID userId = userRepository.save(UserEntity.builder()
                .email("test@mail.ru")
                .password("111")
                .role(Role.ADMIN)
                .build()).getId();

        CustomUserDetails userDetails = new CustomUserDetails(userId, Role.ADMIN);
        JwtAuthentication jwtAuthentication = new JwtAuthentication("jwt-token-123");
        jwtAuthentication.setUserDetails(userDetails);
        jwtAuthentication.setAuthenticated(true);

        SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
    }

    protected TaskEntity generateHighTaskEntity() {
        UUID userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getId();
        return TaskEntity.builder()
                .title("test")
                .description("test")
                .status(Status.FOR_EXECUTION)
                .priority(Priority.HIGH)
                .authorId(userId)
                .executorId(userId)
                .createdAt(LocalDateTime.now())

                .build();
    }

    protected TaskEntity generateLowTaskEntity() {
        UUID userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getId();
        return TaskEntity.builder()
                .title("test")
                .description("test")
                .status(Status.FOR_EXECUTION)
                .priority(Priority.LOW)
                .authorId(userId)
                .executorId(userId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    protected CommentEntity generateCommentEntity(TaskEntity task) {
        UUID userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getId();
        return CommentEntity.builder()
                .comment("test")
                .task(task)
                .createdAt(LocalDateTime.now())
                .authorId(userId)
                .build();
    }
}
