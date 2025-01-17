package ru.vazhmax.task.tracker.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vazhmax.task.tracker.store.entity.ProjectEntity;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findByName(String name);

    Stream<ProjectEntity> streamAllByNameStartsWithIgnoreCase(String prefix);

    Stream<ProjectEntity> streamAll();
}
