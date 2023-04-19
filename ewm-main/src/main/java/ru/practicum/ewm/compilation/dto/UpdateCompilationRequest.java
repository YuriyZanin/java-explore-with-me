package ru.practicum.ewm.compilation.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UpdateCompilationRequest {
    List<Long> events;
    Boolean pinned;
    String title;
}
