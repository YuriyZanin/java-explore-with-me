package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;

import java.util.Collection;

public interface CommentService {
    CommentDto create(Long userId, Long eventId, NewCommentDto commentDto);

    CommentDto update(Long userId, Long commentId, NewCommentDto commentDto);

    void delete(Long commentId);

    CommentDto get(Long userId, Long commentId);

    Collection<CommentDto> getByUser(Long userId, Integer from, Integer size);

    Collection<CommentDto> getByEvent(Long eventId, Integer from, Integer size);
}
