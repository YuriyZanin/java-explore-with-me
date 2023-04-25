package ru.practicum.ewm.comment.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class NewCommentDto {
    Long id;
    @NotNull
    String message;
}
