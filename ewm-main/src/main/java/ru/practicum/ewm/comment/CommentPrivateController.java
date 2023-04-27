package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.service.CommentService;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/comment")
@RequiredArgsConstructor
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> create(@PathVariable Long userId,
                                             @RequestParam Long eventId, @RequestBody NewCommentDto commentDto) {
        log.info("Добавление комментария: userId={}, eventId={}, body={}", userId, eventId, commentDto);
        return new ResponseEntity<>(commentService.create(userId, eventId, commentDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable Long userId,
                             @PathVariable Long commentId, @RequestBody NewCommentDto commentDto) {
        log.info("Обновление комментария: userId={}, commentId={}, body={}", userId, commentId, commentDto);
        return commentService.update(userId, commentId, commentDto);
    }

    @GetMapping("/{commentId}")
    public CommentDto findById(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("Запрос комментария: userId={}, eventId={}", userId, commentId);
        return commentService.get(userId, commentId);
    }
}
