package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        List<UserDto> usersDto = new ArrayList<>();

        if (!users.isEmpty()) {
            for (User user : users) {
                usersDto.add(UserMapper.toUserDto(user));
            }
        }

        return usersDto;
    }

    @Override
    public UserDto getUserById(Long userId) {
        checkIfUserExists(userId);
        return UserMapper.toUserDto(userRepository.getUserById(userId));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (isEmailUnique(user.getEmail())) {
            return UserMapper.toUserDto(userRepository.create(user));
        } else {
            throw new AlreadyExistsException(String.format("User with email=%s already exists", user.getEmail()));
        }
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        checkIfUserExists(userId);

        User user = UserMapper.toUser(userDto);
        if (user.getEmail() == null) {
            user.setEmail(userRepository.getUserById(userId).getEmail());
        }

        if (user.getName() == null) {
            user.setName(userRepository.getUserById(userId).getName());
        }

        user.setId(userId);
        if(isEmailUnique(user.getEmail())) {
            return UserMapper.toUserDto(userRepository.update(userId, user));
        } else if (userRepository.getUserById(userId).getEmail().equals(user.getEmail())) {
            return UserMapper.toUserDto(userRepository.update(userId, user));
        } else {
            throw new AlreadyExistsException(String.format("User with email=%s already exists", user.getEmail()));
        }
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAllUsers();
    }

    @Override
    public void deleteUserById(Long userId) {
        checkIfUserExists(userId);

        userRepository.deleteUserById(userId);
    }

    @Override
    public void checkIfUserExists(Long userId) {
        if (!userRepository.isUserExist(userId) ) {
            throw new NotFoundException(String.format("User with id=%d not found", userId));
        }
    }

    private Boolean isEmailUnique(String email) {
        List<User> users = userRepository.getAllUsers();

        if (users.isEmpty()) {
            return true;
        } else {
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    return false;
                }
            }
        }

        return true;
    }
}