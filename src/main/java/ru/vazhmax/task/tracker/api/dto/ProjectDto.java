package ru.vazhmax.task.tracker.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ProjectDto {
    @NonNull
    Long id;

    @NonNull
    String name;

    @NonNull
    @JsonProperty("created_at")
    private Instant createdAt = Instant.now();
}
