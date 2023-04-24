package ru.practicum.ewm.request.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.request.ParticipationRequestController;
import ru.practicum.ewm.request.service.ParticipationRequestService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParticipationRequestController.class)
public class ParticipationRequestControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ParticipationRequestService requestService;

    @SneakyThrows
    @Test
    void shouldReturn400StatusWhenParamsEmpty() {
        mvc.perform(post("/users/1/requests")).andExpect(status().isBadRequest());
    }
}
