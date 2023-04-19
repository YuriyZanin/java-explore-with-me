package ru.practicum.ewm.compilation.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

@Value
@Builder
public class CompilationDto {
    Long id;
    List<EventShortDto> events;
    boolean pinned;
    String title;
}
