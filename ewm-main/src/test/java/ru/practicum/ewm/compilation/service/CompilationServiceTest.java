package ru.practicum.ewm.compilation.service;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.ewm.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CompilationServiceTest {
    private final CompilationService compilationService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final UserService userService;

    @Test
    void shouldSaveAndGetCompilations() {
        NewCompilationDto dto = NewCompilationDto.builder().title("test").pinned(false).build();
        CompilationDto createdCompilation = compilationService.create(dto);

        NewUserRequest userDetails = NewUserRequest.builder().name("test1").email("mail1@mail.ru").build();
        UserDto createdUser = userService.create(userDetails);

        CategoryDto category1 = CategoryDto.builder().name("category1").build();
        CategoryDto createdCategory = categoryService.create(category1);
        NewEventDto eventCreationDto = NewEventDto.builder()
                .annotation("annotation")
                .description("description")
                .eventDate(LocalDateTime.now().plusDays(3).format(DateTimeUtils.DEFAULT_DATE_TIME_FORMATTER))
                .paid(true)
                .participantLimit(5)
                .requestModeration(true)
                .category(createdCategory.getId())
                .title("title")
                .location(LocationDto.builder().lat(33.343F).lon(34.242F).build())
                .build();

        EventFullDto createdEvent = eventService.create(createdUser.getId(), eventCreationDto);
        UpdateCompilationRequest updateRequest = UpdateCompilationRequest.builder().title("updated").pinned(true)
                .events(List.of(createdEvent.getId())).build();
        compilationService.update(createdCompilation.getId(), updateRequest);
        CompilationDto updatedCompilation = compilationService.get(createdCompilation.getId());

        assertThat(createdCompilation.getId(), notNullValue());
        assertThat(createdCompilation.getTitle(), equalTo(dto.getTitle()));
        assertThat(updatedCompilation.getTitle(), equalTo(updateRequest.getTitle()));
        assertThat(updatedCompilation.getEvents(), hasSize(1));
    }
}
