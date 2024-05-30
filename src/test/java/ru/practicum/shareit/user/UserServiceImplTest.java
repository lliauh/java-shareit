package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User user;
    private UserDto dto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test user");
        user.setEmail("test@mail.nl");

        dto = new UserDto(null, "Test user", "test@mail.nl");
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).
                thenReturn(new ArrayList<>());

        List<UserDto> result = userService.getAllUsers();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testGetUserById() {
        when(userRepository.getReferenceById(any()))
                .thenReturn(user);
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        UserDto result = userService.getUserById(user.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), user.getId());
        Assertions.assertEquals(result.getName(), user.getName());
        Assertions.assertEquals(result.getEmail(), user.getEmail());
        verify(userRepository, times(1)).getReferenceById(user.getId());
        verify(userRepository, times(1)).existsById(user.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testCreate() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.create(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
        Assertions.assertEquals(dto.getEmail(), result.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testUpdate() {
        UserDto updatedDto = new UserDto(null, "Updated", "updated@test.ts");
        User updatedUser = new User("Updated", "updated@test.ts");
        updatedUser.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userRepository.getReferenceById(anyLong())).thenReturn(user);
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        UserDto result = userService.update(1L, updatedDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(updatedDto.getName(), result.getName());
        Assertions.assertEquals(updatedDto.getEmail(), result.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).getReferenceById(anyLong());
        verify(userRepository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testDeleteAllUsers() {
        userService.deleteAllUsers();

        verify(userRepository, times(1)).deleteAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testDeleteUserById() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);
        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(anyLong());
        verify(userRepository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testCheckIfUserExists() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.checkIfUserExists(1L);
        });

        verify(userRepository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }
}