package ru.practicum.stats.endpointhit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.endpointhit.service.EndpointHitService;
import ru.practicum.stats.utils.DateTimeUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        EndpointHitDto test = EndpointHitDto.builder().app("test").uri("test/1").ip("0.0.0.0").timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeUtils.DEFAULT_DATE_TIME_FORMATTER)).build();

        mvc.perform(post("/hit")
                .content(mapper.writeValueAsString(test))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}
