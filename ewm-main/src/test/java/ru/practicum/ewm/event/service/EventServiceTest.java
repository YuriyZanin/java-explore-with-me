package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.configuration.StatsClientConfig;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.dto.NewUserRequestDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.ewm.utils.DateTimeUtils;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {"app.name=ewm-main", "ewm-stats-service-url=http://localhost:9090"})
public class EventServiceTest {
    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;
    @MockBean
    private StatsClient statsClient;
    @MockBean
    private StatsClientConfig config;

    @Test
    void shouldSaveAndGetEvents() {
        CategoryDto category1 = CategoryDto.builder().name("category1").build();
        CategoryDto createdCategory = categoryService.create(category1);

        NewUserRequestDto user1Details = NewUserRequestDto.builder().name("test1").email("mail1@mail.ru").build();
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

        NewEventDto eventCreationDto2 = NewEventDto.builder()
                .annotation("second")
                .description("second")
                .eventDate(LocalDateTime.now().plusDays(2).format(DateTimeUtils.DEFAULT_DATE_TIME_FORMATTER))
                .paid(false)
                .participantLimit(1)
                .requestModeration(false)
                .category(createdCategory.getId())
                .title("second")
                .location(LocationDto.builder().lat(43.3123F).lon(51.422F).build())
                .build();

        UserDto createdUser = userService.create(user1Details);
        EventFullDto createdEvent1 = eventService.create(createdUser.getId(), eventCreationDto);
        EventFullDto createdEvent2 = eventService.create(createdUser.getId(), eventCreationDto2);

        EventRequestParamsDto params = EventRequestParamsDto.builder().paid(false).build();
        List<EventFullDto> searchedByPaid = new ArrayList<>(eventService.getAll(params));

        params = EventRequestParamsDto.builder().text("annotation").build();
        List<EventFullDto> searchedByText = new ArrayList<>(eventService.getAll(params));

        params = EventRequestParamsDto.builder().states(new String[]{"PENDING"}).build();
        List<EventFullDto> searchedPending = new ArrayList<>(eventService.getAll(params));

        params = EventRequestParamsDto.builder().categories(new Long[]{createdCategory.getId()}).build();
        List<EventFullDto> searchedByCategory = new ArrayList<>(eventService.getAll(params));

        UpdateEventRequestDto updateRequest = UpdateEventRequestDto.builder().stateActionDto(StateActionDto.PUBLISH_EVENT).build();
        eventService.updateByAdmin(createdEvent1.getId(), updateRequest);

        params = EventRequestParamsDto.builder().states(new String[]{"PUBLISHED"}).build();
        List<EventFullDto> searchedPublished = new ArrayList<>(eventService.getAll(params));

        Mockito.when(statsClient.getStats(Mockito.any(), Mockito.any(), Mockito.anyList(), Mockito.any()))
                .thenReturn(List.of(ViewStatsDto.builder().app("ewm-main").uri("events/1").hits(1L).build()));
        EventFullDto eventWithViews = eventService.getPublicById(createdEvent1.getId(), "events/1", "0.0.0.0");
        List<EventShortDto> allEvents = new ArrayList<>(
                eventService.getAllPublic(EventRequestParamsDto.builder().build(), "events", "0.0.0.0"));

        assertThat(createdEvent1.getId(), notNullValue());
        assertThat(searchedByPaid, hasSize(1));
        assertThat(searchedByPaid.get(0), hasProperty("title", equalTo(createdEvent2.getTitle())));
        assertThat(searchedByText, hasSize(1));
        assertThat(searchedByText.get(0), hasProperty("title", equalTo(createdEvent1.getTitle())));
        assertThat(searchedPending, hasSize(2));
        assertThat(searchedByCategory, hasSize(2));
        assertThat(searchedPublished, hasSize(1));
        assertThat(searchedPublished.get(0), hasProperty("state", equalTo(EventState.PUBLISHED.name())));
        assertThat(eventWithViews.getId(), equalTo(createdEvent1.getId()));
        assertThat(eventWithViews.getViews(), equalTo(1));
        assertThat(allEvents, hasSize(1));
        assertThat(allEvents.get(0), hasProperty("views", equalTo(1)));
    }
}
