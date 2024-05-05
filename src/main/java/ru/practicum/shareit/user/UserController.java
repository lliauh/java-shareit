package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Getting all users...");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Getting user id={}", userId);
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto user) {
        log.info("Creating new user={}", user);
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId, @RequestBody UserDto user) {
        log.info("Updating user ID={}, updated user={}", userId, user);
        return userService.update(userId, user);
    }

    @DeleteMapping
    public void deleteAllUsers() {
        log.info("Deleting all users...");
        userService.deleteAllUsers();
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.info("Deleting user id={}", userId);
        userService.deleteUserById(userId);
    }
}