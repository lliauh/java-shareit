package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllUsers() throws Exception {
        UserDto user = new UserDto(7L, "TestName" ,"test@mail.ts");

        when(userService.getAllUsers())
                .thenReturn(List.of(user));

        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(user.getName())))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() throws Exception {
        UserDto user = new UserDto(7L, "TestName" ,"test@mail.ts");

        when(userService.getUserById(any()))
                .thenReturn(user);

        mockMvc.perform(get("/users/7")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

        verify(userService, times(1)).getUserById(user.getId());
    }

    @Test
    void testCreate() throws Exception {
        UserDto userIn = new UserDto(7L, "TestName" ,"test@mail.ts");
        UserDto userOut  = new UserDto(7L, "TestName" ,"test@mail.ts");

        when(userService.create(any()))
                .thenReturn(userOut);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userOut.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userOut.getName())))
                .andExpect(jsonPath("$.email", is(userOut.getEmail())));

        verify(userService, times(1)).create(userIn);
    }

    @Test
    void testUpdate() throws Exception {
        UserDto userIn = new UserDto(7L, "TestName" ,"test@mail.ts");
        UserDto userOut  = new UserDto(7L, "UpdatedName" ,"updated_test@mail.ts");

        when(userService.update(any(), any()))
                .thenReturn(userOut);

        mockMvc.perform(patch("/users/7")
                        .content(mapper.writeValueAsString(userIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userOut.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userOut.getName())))
                .andExpect(jsonPath("$.email", is(userOut.getEmail())));

        verify(userService, times(1)).update(7L, userIn);
    }

    @Test
    void testDeleteAllUsers() throws Exception {
        mockMvc.perform(delete("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());

        verify(userService, times(1)).deleteAllUsers();
    }

    @Test
    void testDeleteUserById() throws Exception {
        mockMvc.perform(delete("/users/7")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(7L);
    }
}