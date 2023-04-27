package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/comment")
@RequiredArgsConstructor
public class CommentAdminController {
    private final CommentService commentService;

    @GetMapping("/{userId}")
    public Collection<CommentDto> findByUser(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос комментариев по пользователю: userId={}, from={}, size={}", userId, from, size);
        return commentService.getByUser(userId, from, size);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> delete(@PathVariable Long commentId) {
        log.info("Удаление комментария: commentId={}", commentId);
        commentService.delete(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
