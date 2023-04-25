package ru.practicum.ewm.comment.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.user.dto.UserShortDto;

@Value
@Builder
public class CommentDto {
    Long id;
    UserShortDto user;
    EventShortDto event;
    String message;
    String createdOn;
}
