package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndGet() {
        User user = new User("Test user", "mail@test.ts");
        user = userRepository.save(user);

        User result = userRepository.getReferenceById(user.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), user.getId());
        Assertions.assertEquals(result.getName(), user.getName());
        Assertions.assertEquals(result.getEmail(), user.getEmail());
    }
}