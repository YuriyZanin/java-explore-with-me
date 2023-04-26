package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.service.EventService;
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
public class CommentServiceTest {
    private final CompilationService compilationService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final UserService userService;
    private final CommentService commentService;

    @Test
    void shouldSaveAndGetComment() {
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
        UserDto createdUser = userService.create(user1Details);
        UserDto created2User = userService.create(user2Details);
        EventFullDto createdEvent1 = eventService.create(createdUser.getId(), eventCreationDto);

        UpdateEventRequestDto updateRequest = UpdateEventRequestDto.builder().stateAction(StateActionDto.PUBLISH_EVENT).build();
        eventService.updateByAdmin(createdEvent1.getId(), updateRequest);

        NewCommentDto newComment = new NewCommentDto(null, "test");
        commentService.create(created2User.getId(), createdEvent1.getId(), newComment);

        Collection<CommentDto> byUser = commentService.getByUser(created2User.getId(), 0, 1);
        Collection<CommentDto> byEvent = commentService.getByEvent(createdEvent1.getId(), 0, 1);

        assertThat(byUser, hasSize(1));
        assertThat(byEvent, hasSize(1));
    }
}
