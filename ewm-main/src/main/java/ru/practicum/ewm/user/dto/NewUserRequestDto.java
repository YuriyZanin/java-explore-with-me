package ru.practicum.ewm.user.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
@Builder
public class NewUserRequestDto {
    @Email
    @NotBlank
    String email;
    @NotBlank
    String name;
}
