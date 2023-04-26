package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public CommentDto create(Long userId, Long eventId, NewCommentDto commentDto) {
        User user = getUser(userId);
        Event event = getEvent(eventId);

        Comment comment = Comment.builder()
                .user(user).event(event).message(commentDto.getMessage()).createdOn(LocalDateTime.now()).build();
        return CommentMapper.MAPPER.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto update(Long userId, Long commentId, NewCommentDto commentDto) {
        Comment comment = getComment(commentId);

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Пользователь может редактировать только свои комментарии");
        }

        comment.setMessage(commentDto.getMessage());
        return CommentMapper.MAPPER.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void delete(Long commentId) {
        Comment comment = getComment(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto get(Long userId, Long commentId) {
        Comment comment = getComment(commentId);

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Комментарий не принадлежит пользователю");
        }

        return CommentMapper.MAPPER.toCommentDto(comment);
    }

    @Override
    public Collection<CommentDto> getByUser(Long userId, Integer from, Integer size) {
        User user = getUser(userId);
        PageRequest page = PageRequest.of(from / size, size);
        List<Comment> byUser = commentRepository.findByUser(user, page);
        return CommentMapper.MAPPER.toCommentDtos(byUser);
    }

    @Override
    public Collection<CommentDto> getByEvent(Long eventId, Integer from, Integer size) {
        Event event = getEvent(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new IllegalArgumentException("Событие не опубликовано");
        }

        PageRequest page = PageRequest.of(from / size, size);
        return CommentMapper.MAPPER.toCommentDtos(commentRepository.findByEvent(event, page));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
    }
}
