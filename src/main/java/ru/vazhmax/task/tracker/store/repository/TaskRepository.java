package ru.vazhmax.task.tracker.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vazhmax.task.tracker.store.entity.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
