package ru.practicum.ewm.compilation.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Value
@Builder
public class NewCompilationDto {
    List<Long> events;
    Boolean pinned;
    @NotBlank
    String title;
}
