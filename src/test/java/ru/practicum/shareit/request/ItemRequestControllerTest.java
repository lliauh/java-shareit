package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRequestItem() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto(5L, "Test request", LocalDateTime.now()
                .withNano(0));

        when(itemRequestService.requestItem(any(), any()))
                .thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 10)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.created", is(requestDto.getCreated().toString())));

        verify(itemRequestService, times(1)).requestItem(10L, requestDto);
    }

    @Test
    void testGetAllUserRequests() throws Exception {
        ItemRequestOutDto requestOutDto = new ItemRequestOutDto(5L, "Test request", LocalDateTime.now()
                .withNano(0));

        when(itemRequestService.getAllUserRequests(any()))
                .thenReturn(List.of(requestOutDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 10)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestOutDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestOutDto.getDescription())))
                .andExpect(jsonPath("$[0].created", is(requestOutDto.getCreated().toString())));

        verify(itemRequestService, times(1)).getAllUserRequests(10L);
    }

    @Test
    void testGetAllRequests() throws Exception {
        ItemRequestOutDto requestOutDto = new ItemRequestOutDto(5L, "Test request",
                LocalDateTime.now().withNano(0));

        when(itemRequestService.getAllRequests(any(), any(), any()))
                .thenReturn(List.of(requestOutDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 10)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestOutDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestOutDto.getDescription())))
                .andExpect(jsonPath("$[0].created", is(requestOutDto.getCreated().toString())));

        verify(itemRequestService, times(1)).getAllRequests(10L, 0, 10);
    }

    @Test
    void testGetRequestById() throws Exception {
        ItemRequestOutDto requestOutDto = new ItemRequestOutDto(5L, "Test request", LocalDateTime.now()
                .withNano(0));

        when(itemRequestService.getRequestById(any(), any()))
                .thenReturn(requestOutDto);

        mockMvc.perform(get("/requests/5")
                        .header("X-Sharer-User-Id", 10)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestOutDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestOutDto.getDescription())))
                .andExpect(jsonPath("$.created", is(requestOutDto.getCreated().toString())));

        verify(itemRequestService, times(1)).getRequestById(10L, 5L);
    }
}