package ru.practicum.ewm.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAdminController.class)
public class UserAdminControllerTest {
    private final UserDto userDto = UserDto.builder().name("test").email("test@mail.com").build();
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @SneakyThrows
    @Test
    void shouldReturnStatus200AndJsonWhenSaveNewUser() {
        NewUserRequest userRequest = NewUserRequest.builder().name("test").email("test@mail.com").build();

        Mockito.when(userService.create(Mockito.any())).thenReturn(userDto);

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @SneakyThrows
    @Test
    void shouldReturnStatus400WhenNotValidEmail() {
        NewUserRequest userRequest = NewUserRequest.builder().email("notValidMail").name("test").build();

        mvc.perform(post("/admin/users")
                .content(mapper.writeValueAsString(userRequest))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldReturnStatus400WhenNameIsNull() {
        NewUserRequest userRequest = NewUserRequest.builder().name(null).email("test@mail.com").build();

        mvc.perform(post("/admin/users")
                .content(mapper.writeValueAsString(userRequest))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldReturnStatus409IfAlreadyExist() {
        Mockito.when(userService.create(Mockito.any())).thenThrow(DataIntegrityViolationException.class);

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CONFLICT.value()));
    }

    @SneakyThrows
    @Test
    void shouldReturnStatus204WhenDeleteUser() {
        mvc.perform(delete("/admin/users/1")).andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void shouldReturnStatus404WhenDeletingNotExist() {
        Mockito.doThrow(NotFoundException.class).when(userService).delete(Mockito.anyLong());

        mvc.perform(delete("/admin/users/1")).andExpect(status().isNotFound());
    }
}
