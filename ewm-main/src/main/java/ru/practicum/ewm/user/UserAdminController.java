package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> findAll(@RequestParam(required = false) Long[] ids,
                                       @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                       @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос информации о пользователях ids={}, from={}, size={}", ids, from, size);
        return userService.getAll(ids, from, size);
    }

    @PostMapping
    public UserDto create(@RequestBody @Validated NewUserRequest userDetails) {
        log.info("Запрос на добавление пользователя {}", userDetails);
        return userService.create(userDetails);
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<?> delete(@PathVariable Long userId) {
        log.info("Запрос на удаление пользователя {}", userId);
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
