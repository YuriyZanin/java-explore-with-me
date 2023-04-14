package ru.practicum.stats.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.stats.utils.DateTimeUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
@Builder
public class EndpointHitDto {
    Long id;
    @NotBlank
    String app;
    @NotBlank
    String uri;
    @NotBlank
    String ip;
    @NotNull
    @Pattern(regexp = DateTimeUtils.DEFAULT_DATE_TIME_REGEXP)
    String timestamp;
}
