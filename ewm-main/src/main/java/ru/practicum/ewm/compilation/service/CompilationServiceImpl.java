package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequestDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public Collection<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        if (pinned != null) {
            return CompilationMapper.MAPPER.toDtos(compilationRepository.findAllByPinnedIs(pinned, page));
        } else {
            return CompilationMapper.MAPPER.toDtos(compilationRepository.findAll(page).getContent());
        }
    }

    @Override
    public CompilationDto get(Long compId) {
        Compilation compilation = getCompilation(compId);
        return CompilationMapper.MAPPER.toDto(compilation);
    }

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto creationDto) {
        Compilation compilation = CompilationMapper.MAPPER.toCompilation(creationDto);
        return CompilationMapper.MAPPER.toDto(compilationRepository.save(compilation));
    }

    @Transactional
    @Override
    public void delete(Long compId) {
        Compilation compilation = getCompilation(compId);
        compilationRepository.delete(compilation);
    }

    @Transactional
    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequestDto updateRequest) {
        Compilation compilation = getCompilation(compId);

        Compilation request = CompilationMapper.MAPPER.toCompilation(updateRequest);
        Optional.ofNullable(request.getTitle()).ifPresent(compilation::setTitle);
        Optional.ofNullable(request.getPinned()).ifPresent(compilation::setPinned);
        Optional.ofNullable(request.getEvents()).ifPresent(compilation::setEvents);

        return CompilationMapper.MAPPER.toDto(compilationRepository.save(compilation));
    }

    private Compilation getCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));
    }
}
