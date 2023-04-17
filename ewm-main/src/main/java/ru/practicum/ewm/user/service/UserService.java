package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAll(Long[] ids, Integer from, Integer size);

    UserDto create(NewUserRequest userDetails);

    void delete(Long userId);
}
