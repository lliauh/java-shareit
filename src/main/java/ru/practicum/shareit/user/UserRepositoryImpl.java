package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long lastUserId = 0L;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        }
        return null;
    }

    @Override
    public User create(User user) {
        lastUserId++;
        user.setId(lastUserId);

        users.put(user.getId(), user);

        return users.get(user.getId());
    }

    @Override
    public User update(Long userId, User user) {
        users.put(userId, user);

        return users.get(userId);
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }

    @Override
    public void deleteUserById(Long userId) {
        users.remove(userId);
    }

    @Override
    public Boolean isUserExist(Long userId) {
        return users.containsKey(userId);
    }
}