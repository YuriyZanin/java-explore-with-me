package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserServiceTest {
    private final UserService userService;

    @Test
    void shouldSaveAndGetUsers() {
        NewUserRequest user1Details = NewUserRequest.builder().email("mail1@mail.ru").name("test1").build();
        NewUserRequest user2Details = NewUserRequest.builder().email("mail2@mail.ru").name("test2").build();
        NewUserRequest user3Details = NewUserRequest.builder().email("mail3@mail.ru").name("test3").build();

        UserDto createdUser1 = userService.create(user1Details);
        UserDto createdUser2 = userService.create(user2Details);
        UserDto createdUser3 = userService.create(user3Details);

        Collection<UserDto> allUsers = userService.getAll(null, 0, 5);
        Collection<UserDto> usersBy1And3Id = userService
                .getAll(Arrays.array(createdUser1.getId(), createdUser3.getId()), 0, 5);

        userService.delete(createdUser1.getId());
        Collection<UserDto> allUserAfterDelete = userService.getAll(null, 0, 5);

        assertThat(allUsers, not(empty()));
        assertThat(allUsers, hasSize(3));
        assertThat(allUsers, hasItems(createdUser1, createdUser2, createdUser3));
        assertThat(usersBy1And3Id, not(empty()));
        assertThat(usersBy1And3Id, hasSize(2));
        assertThat(usersBy1And3Id, hasItems(createdUser1, createdUser3));
        assertThat(allUserAfterDelete, not(empty()));
        assertThat(allUserAfterDelete, hasSize(2));
        assertThat(allUserAfterDelete, hasItems(createdUser2, createdUser3));
        Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                userService.create(NewUserRequest.builder().email("mail2@mail.ru").name("test2").build()));
    }
}