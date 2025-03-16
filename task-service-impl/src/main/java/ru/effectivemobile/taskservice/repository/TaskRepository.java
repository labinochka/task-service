package ru.effectivemobile.taskservice.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.effectivemobile.taskservice.dto.enumeration.Priority;
import ru.effectivemobile.taskservice.dto.enumeration.Status;
import ru.effectivemobile.taskservice.entity.TaskEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID>, JpaSpecificationExecutor<TaskEntity> {

    default Set<TaskEntity> findAllByFilters(List<Status> statuses,
                                             List<Priority> priorities,
                                             List<UUID> authorIds,
                                             List<UUID> executorIds,
                                             String search) {
        return new HashSet<>(findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (statuses != null && !statuses.isEmpty()) {
                predicates.add(criteriaBuilder.in(root.get("status")).value(statuses));
            }

            if (priorities != null && !priorities.isEmpty()) {
                predicates.add(criteriaBuilder.in(root.get("priority")).value(priorities));
            }

            if (authorIds != null && !authorIds.isEmpty()) {
                predicates.add(criteriaBuilder.in(root.get("authorId")).value(authorIds));
            }

            if (executorIds != null && !executorIds.isEmpty()) {
                predicates.add(criteriaBuilder.in(root.get("executorId")).value(executorIds));
            }

            if (search != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                        "%" + search.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }));
    }
}
