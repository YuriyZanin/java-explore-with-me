package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentPublicController {
    private final CommentService commentService;

    @GetMapping("/{eventId}")
    public Collection<CommentDto> findByEvent(@PathVariable Long eventId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос комментов по событию: eventId={}, from={}, size={}", eventId, from, size);
        return commentService.getByEvent(eventId, from, size);
    }
}
