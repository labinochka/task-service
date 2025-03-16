package ru.effectivemobile.taskservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.effectivemobile.taskservice.entity.CommentEntity;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
}
