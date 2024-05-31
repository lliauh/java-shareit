package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

public class UserMapperTest {

    @Test
    void toUserDtoTest() {
        User user = new User();
        user.setId(2L);
        user.setName("Test name");
        user.setEmail("mail@test.ts");

        UserDto result = UserMapper.toUserDto(user);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), user.getId());
        Assertions.assertEquals(result.getName(), user.getName());
        Assertions.assertEquals(result.getEmail(), user.getEmail());
    }

    @Test
    void toUserTest() {
        UserDto dto = new UserDto(1L, "Test2 name", "mail2@test.ts");
        User result = UserMapper.toUser(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getName(), dto.getName());
        Assertions.assertEquals(result.getEmail(), dto.getEmail());
    }
}