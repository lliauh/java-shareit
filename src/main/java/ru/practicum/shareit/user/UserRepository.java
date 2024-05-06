package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;
import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();

    User getUserById(Long userId);

    User create(User user);

    User update(Long userId, User user);

    void deleteAllUsers();

    void deleteUserById(Long userId);

    Boolean isUserExist(Long userId);
}
