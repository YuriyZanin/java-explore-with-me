package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.dto.NewUserRequestDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;

import static ru.practicum.ewm.user.mapper.UserMapper.MAPPER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> getAll(Long[] ids, Integer from, Integer size) {
        return MAPPER.toUserDtos(ids == null
                ? userRepository.findAll(PageRequest.of(from / size, size)).getContent()
                : userRepository.findAllById(Arrays.asList(ids)));
    }

    @Transactional
    @Override
    public UserDto create(NewUserRequestDto userDetails) {
        User user = MAPPER.toUser(userDetails);
        return MAPPER.toUserDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " was not found"));

        userRepository.delete(user);
    }
}
