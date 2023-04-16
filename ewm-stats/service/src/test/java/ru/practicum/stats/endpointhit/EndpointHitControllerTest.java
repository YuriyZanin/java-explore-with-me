package ru.practicum.stats.endpointhit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.endpointhit.service.EndpointHitService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.stats.utils.DateTimeUtils.DEFAULT_DATE_TIME_FORMATTER;

@WebMvcTest(EndpointHitController.class)
public class EndpointHitControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private EndpointHitService endpointHitService;

    @SneakyThrows
    @Test
    void saveRequest() {
        EndpointHitDto test = EndpointHitDto.builder().app("test").uri("test/1").ip("0.0.0.0")
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DEFAULT_DATE_TIME_FORMATTER))
                .build();

        mvc.perform(post("/hit")
                .content(mapper.writeValueAsString(test))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void findStats() {
        ViewStatsDto test = ViewStatsDto.builder().app("/test-app").uri("/test/1").hits(6L).build();

        Mockito.when(endpointHitService.getStats(
                        Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyBoolean()))
                .thenReturn(List.of(test));

        mvc.perform(get("/stats")
                        .param("start", LocalDateTime.now().format(DEFAULT_DATE_TIME_FORMATTER))
                        .param("end", LocalDateTime.now().format(DEFAULT_DATE_TIME_FORMATTER))
                        .param("unique", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].app", equalTo("/test-app")))
                .andExpect(jsonPath("$.[0].uri", equalTo("/test/1")))
                .andExpect(jsonPath("$.[0].hits", equalTo(6)));
    }

    @SneakyThrows
    @Test
    void shouldBeFailedIfAppNull() {
        EndpointHitDto test = EndpointHitDto.builder().app(null).uri("test/1").ip("0.0.0.0")
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DEFAULT_DATE_TIME_FORMATTER))
                .build();

        mvc.perform(post("/hit")
                .content(mapper.writeValueAsString(test))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @SneakyThrows
    @Test
    void shouldBeFailedIfUriBlank() {
        EndpointHitDto test = EndpointHitDto.builder().app("test").uri("    ").ip("0.0.0.0")
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DEFAULT_DATE_TIME_FORMATTER))
                .build();

        mvc.perform(post("/hit")
                .content(mapper.writeValueAsString(test))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @SneakyThrows
    @Test
    void shouldBeFailedIfIpEmpty() {
        EndpointHitDto test = EndpointHitDto.builder().app("test").uri("/test/1").ip("")
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DEFAULT_DATE_TIME_FORMATTER))
                .build();

        mvc.perform(post("/hit")
                .content(mapper.writeValueAsString(test))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @SneakyThrows
    @Test
    void shouldBeFailedIfDateFormatIncorrect() {
        EndpointHitDto test = EndpointHitDto.builder().app("test").uri("/test/1").ip("0.0.0.0")
                .timestamp("2023-04-14")
                .build();

        mvc.perform(post("/hit")
                .content(mapper.writeValueAsString(test))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }
}
