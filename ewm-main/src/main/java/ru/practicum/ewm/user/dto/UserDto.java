package ru.practicum.ewm.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {
    String email;
    Long id;
    String name;
}
