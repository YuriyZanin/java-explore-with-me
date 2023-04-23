package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.dto.NewUserRequestDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.ewm.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {"app.name=ewm-main", "ewm-stats-service-url=http://localhost:9090"})
public class ParticipationRequestServiceTest {
    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ParticipationRequestService requestService;

    @Test
    void shouldSaveAndGetRequests() {
        CategoryDto category1 = CategoryDto.builder().name("category1").build();
        CategoryDto createdCategory = categoryService.create(category1);

        NewUserRequestDto user1Details = NewUserRequestDto.builder().name("test1").email("mail1@mail.ru").build();
        NewUserRequestDto user2Details = NewUserRequestDto.builder().name("test2").email("mail2@mail.ru").build();

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

        UserDto createdUser1 = userService.create(user1Details);
        UserDto createdUser2 = userService.create(user2Details);

        EventFullDto createdEvent1 = eventService.create(createdUser1.getId(), eventCreationDto);
        UpdateEventRequestDto updateRequest = UpdateEventRequestDto.builder().stateAction(StateActionDto.PUBLISH_EVENT).build();
        eventService.updateByAdmin(createdEvent1.getId(), updateRequest);

        requestService.create(createdUser2.getId(), createdEvent1.getId());
        Collection<ParticipationRequestDto> byUser = requestService.getByUser(createdUser2.getId());
        Collection<ParticipationRequestDto> byEvent = eventService.getRequests(createdUser1.getId(), createdEvent1.getId());

        assertThat(byUser, hasSize(1));
        assertThat(byEvent, hasSize(1));
    }
}
