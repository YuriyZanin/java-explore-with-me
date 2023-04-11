package ru.practicum.stats.endpointhit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.practicum.stats.utils.DateTimeUtils.DEFAULT_DATE_TIME_FORMATTER;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class EndpointHitServiceTest {
    private final EndpointHitService endpointHitService;

    @Test
    void shouldSaveAndFindStats() {
        LocalDateTime currentDate = LocalDateTime.now();
        EndpointHitDto test1 = EndpointHitDto.builder()
                .app("test_app").uri("/test/1").ip("0.0.0.1").timestamp(currentDate).build();
        EndpointHitDto test2 = EndpointHitDto.builder()
                .app("test_app").uri("/test/1").ip("0.0.0.2").timestamp(currentDate).build();
        EndpointHitDto test3 = EndpointHitDto.builder()
                .app("test_app").uri("/test/2").ip("0.0.0.1").timestamp(currentDate.minusDays(1)).build();
        EndpointHitDto test4 = EndpointHitDto.builder()
                .app("test_app").uri("/test/2").ip("0.0.0.1").timestamp(currentDate.minusDays(2)).build();

        endpointHitService.create(test1);
        endpointHitService.create(test2);
        endpointHitService.create(test3);
        endpointHitService.create(test4);

        List<ViewStatsDto> stats = endpointHitService.getStats(currentDate.minusMinutes(5).format(DEFAULT_DATE_TIME_FORMATTER),
                currentDate.plusMinutes(5).format(DEFAULT_DATE_TIME_FORMATTER), null, false);

        List<ViewStatsDto> statsUnique = endpointHitService.getStats(currentDate.minusDays(6).format(DEFAULT_DATE_TIME_FORMATTER),
                currentDate.plusDays(6).format(DEFAULT_DATE_TIME_FORMATTER), null, true);

        List<ViewStatsDto> statsByUris = endpointHitService.getStats(currentDate.minusDays(6).format(DEFAULT_DATE_TIME_FORMATTER),
                currentDate.plusDays(6).format(DEFAULT_DATE_TIME_FORMATTER), List.of("/test/1"), true);

        List<ViewStatsDto> byDateNotUnique = endpointHitService.getStats(currentDate.minusDays(2).format(DEFAULT_DATE_TIME_FORMATTER),
                currentDate.format(DEFAULT_DATE_TIME_FORMATTER), null, false);

        assertThat(stats, notNullValue());
        assertThat(stats, hasSize(1));
        assertThat(stats.get(0), hasProperty("app", equalTo("test_app")));
        assertThat(stats.get(0), hasProperty("uri", equalTo("/test/1")));
        assertThat(stats.get(0), hasProperty("hits", equalTo(2L)));
        assertThat(statsUnique, notNullValue());
        assertThat(statsUnique, hasSize(2));
        assertThat(statsUnique.get(0), hasProperty("uri", equalTo("/test/1")));
        assertThat(statsUnique.get(1), hasProperty("uri", equalTo("/test/2")));
        assertThat(statsUnique.get(1), hasProperty("hits", equalTo(1L)));
        assertThat(statsByUris, notNullValue());
        assertThat(statsByUris, hasSize(1));
        assertThat(statsByUris.get(0), hasProperty("uri", equalTo("/test/1")));
        assertThat(statsByUris.get(0), hasProperty("hits", equalTo(2L)));
        assertThat(byDateNotUnique, hasSize(1));
        assertThat(byDateNotUnique.get(0), hasProperty("uri", equalTo("/test/2")));
        assertThat(byDateNotUnique.get(0), hasProperty("hits", equalTo(2L)));
    }
}
