package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        checkIfUserExists(userId);
        return UserMapper.toUserDto(userRepository.getReferenceById(userId));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);

        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        checkIfUserExists(userId);

        User updatedUser = UserMapper.toUser(userDto);
        User currentUser = userRepository.getReferenceById(userId);

        if (updatedUser.getEmail() == null) {
            updatedUser.setEmail(currentUser.getEmail());
        }

        if (updatedUser.getName() == null) {
            updatedUser.setName(currentUser.getName());
        }

        updatedUser.setId(userId);

        return UserMapper.toUserDto(userRepository.save(updatedUser));
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public void deleteUserById(Long userId) {
        checkIfUserExists(userId);

        userRepository.deleteById(userId);
    }

    @Override
    public void checkIfUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%d not found", userId));
        }
    }
}