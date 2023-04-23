package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.NewUserRequestDto;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAll(Long[] ids, Integer from, Integer size);

    UserDto create(NewUserRequestDto userDetails);

    void delete(Long userId);
}
