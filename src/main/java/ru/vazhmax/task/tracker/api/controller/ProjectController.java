package ru.vazhmax.task.tracker.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.vazhmax.task.tracker.Exceptions.BadRequestException;
import ru.vazhmax.task.tracker.Exceptions.NotFoundException;
import ru.vazhmax.task.tracker.api.dto.AckDto;
import ru.vazhmax.task.tracker.api.dto.ProjectDto;
import ru.vazhmax.task.tracker.store.entity.ProjectEntity;
import ru.vazhmax.task.tracker.store.repository.ProjectRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
@RestController
public class ProjectController {

    ModelMapper modelMapper;
    ProjectRepository projectRepository;

    public static final String FETCH_PROJECTS = "/api/projects";
    public static final String CREATE_PROJECT = "/api/projects";
    public static final String EDIT_PROJECT = "/api/projects/{project_id}";
    public static final String DELETE_PROJECT = "/api/projects/{project_id}";

    @GetMapping(FETCH_PROJECTS)
    public List<ProjectDto> fetchProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefix) {

        optionalPrefix = optionalPrefix.filter(prefix -> !prefix.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefix
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAll);

        return projectStream.map(project -> modelMapper.map(project, ProjectDto.class)).toList();
    }


    @Transactional
    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(@RequestParam String name) {

        if(name.trim().isEmpty()){
            throw new BadRequestException("Name can't be empty");
        }

        projectRepository
                .findByName(name)
                .ifPresent(p -> {
                    throw new BadRequestException(String.format("Project %s already exists", p.getName()));
                });

        ProjectEntity project = projectRepository.saveAndFlush(ProjectEntity.builder().name(name).build());

        return modelMapper.map(project, ProjectDto.class);
    }

    @Transactional
    @PatchMapping(EDIT_PROJECT)
    public ProjectDto editProject(
            @PathVariable("project_id") Long projectId,
            @RequestParam String name) {

        if(name.trim().isEmpty()){
            throw new BadRequestException("Name can't be empty");
        }

        ProjectEntity project = getProjectOrThrowException(projectId);

        projectRepository.findByName(name)
                .filter(anotherProject -> !Objects.equals(anotherProject.getId(), projectId))
                .ifPresent(anotherProject -> {
                    throw new BadRequestException(String.format("Project %s already exists", name));
                });

        project.setName(name);

        project = projectRepository.saveAndFlush(project);

        return modelMapper.map(project, ProjectDto.class);
    }

    @Transactional
    @DeleteMapping(DELETE_PROJECT)
    public AckDto deleteProject(@PathVariable("project_id") Long projectId){

        getProjectOrThrowException(projectId);

        projectRepository.deleteById(projectId);

        return AckDto.makeDefault(true);
    }

    private ProjectEntity getProjectOrThrowException(Long projectId) {

        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Project with id %s doesn't exist", projectId)
                        )
                );
    }
}
