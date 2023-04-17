package ru.practicum.ewm.user.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
public class NewUserRequest {
    @Email
    @NotBlank
    String email;
    @NotBlank(message = "must not be blank")
    String name;
}
