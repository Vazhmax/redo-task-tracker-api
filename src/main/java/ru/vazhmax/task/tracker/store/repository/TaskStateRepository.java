package ru.vazhmax.task.tracker.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vazhmax.task.tracker.store.entity.TaskStateEntity;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {
}
