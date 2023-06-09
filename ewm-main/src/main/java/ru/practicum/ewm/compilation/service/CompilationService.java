package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequestDto;

import java.util.Collection;

public interface CompilationService {
    Collection<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto get(Long compId);

    CompilationDto create(NewCompilationDto creationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequestDto request);
}
