package ru.practicum.ewm.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequestDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

@Mapper
public interface CompilationMapper {
    CompilationMapper MAPPER = Mappers.getMapper(CompilationMapper.class);

    @Named("toEvent")
    static Event toEvent(Long id) {
        return Event.builder().id(id).build();
    }

    CompilationDto toDto(Compilation compilation);

    List<CompilationDto> toDtos(List<Compilation> compilations);

    @Mapping(source = "events", target = "events", qualifiedByName = "toEvent")
    Compilation toCompilation(NewCompilationDto dto);

    @Mapping(source = "events", target = "events", qualifiedByName = "toEvent")
    Compilation toCompilation(UpdateCompilationRequestDto request);
}
