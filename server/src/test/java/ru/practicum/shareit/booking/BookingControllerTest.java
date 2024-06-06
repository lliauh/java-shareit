package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.BookingSearchState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testBookItem() throws Exception {
        ItemDto item = new ItemDto(12L, "Item1", "Test_description", true);
        UserDto booker  = new UserDto(7L, "TestName", "test@mail.ts");
        BookingOutDto outDto = new BookingOutDto(10L, LocalDateTime.now().withNano(0),
                LocalDateTime.now().withNano(0), item, booker,
                BookingStatus.WAITING);
        BookingDto inDto = new BookingDto(null, 12L, LocalDateTime.now().withNano(0),
                LocalDateTime.now().withNano(0));

        when(bookingService.bookItem(any(), any()))
                .thenReturn(outDto);

        mockMvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", booker.getId())
                .content(mapper.writeValueAsString(inDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(outDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(outDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(outDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(outDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(outDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(outDto.getItem().getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(outDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(outDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(outDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.status", is(outDto.getStatus().toString())));

        verify(bookingService, times(1)).bookItem(booker.getId(), inDto);
    }

    @Test
    void testProcessRequest() throws Exception {
        ItemDto item = new ItemDto(12L, "Item1", "Test_description", true);
        UserDto booker  = new UserDto(7L, "TestName", "test@mail.ts");
        BookingOutDto outDto = new BookingOutDto(10L, LocalDateTime.now().withNano(0),
                LocalDateTime.now().withNano(0), item, booker,
                BookingStatus.APPROVED);

        when(bookingService.processRequest(10L, true, 1L))
                .thenReturn(outDto);

        mockMvc.perform(patch("/bookings/10")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", String.valueOf(true))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(outDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(outDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(outDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(outDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(outDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(outDto.getItem().getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(outDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(outDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(outDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.status", is(outDto.getStatus().toString())));

        verify(bookingService, times(1)).processRequest(10L, true, 1L);
    }

    @Test
    void testGetBooking() throws Exception {
        ItemDto item = new ItemDto(12L, "Item1", "Test_description", true);
        UserDto booker  = new UserDto(7L, "TestName", "test@mail.ts");
        BookingOutDto outDto = new BookingOutDto(10L, LocalDateTime.now().withNano(0),
                LocalDateTime.now().withNano(0), item, booker,
                BookingStatus.REJECTED);

        when(bookingService.getBookingById(any(), any()))
                .thenReturn(outDto);

        mockMvc.perform(get("/bookings/10")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(outDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(outDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(outDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(outDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(outDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(outDto.getItem().getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(outDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(outDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(outDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.status", is(outDto.getStatus().toString())));

        verify(bookingService, times(1)).getBookingById(10L, 1L);
    }

    @Test
    void testGetUserBookingsByState() throws Exception {
        ItemDto item = new ItemDto(12L, "Item1", "Test_description", true);
        UserDto booker  = new UserDto(7L, "TestName", "test@mail.ts");
        BookingOutDto outDto = new BookingOutDto(10L, LocalDateTime.now().withNano(0),
                LocalDateTime.now().withNano(0), item, booker,
                BookingStatus.WAITING);
        List<BookingOutDto> bookings = List.of(outDto);

        when(bookingService.getUserBookingsByState(any(), any(), any(), any()))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 7)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(outDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(outDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(outDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", is(outDto.getStatus().toString())))
                .andExpect(jsonPath("$[0].item.id", is(outDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(outDto.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", is(outDto.getItem().getDescription())));

        verify(bookingService, times(1)).getUserBookingsByState(BookingSearchState.ALL,
                7L, 0, 10);
    }

    @Test
    void testGetBookingsOnUserItemsByState() throws Exception {
        List<BookingOutDto> bookings = new ArrayList<>();

        when(bookingService.getBookingsOnUserItemsByState(any(), any(), any(), any()))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                .header("X-Sharer-User-Id", 9)
                .param("state", "CURRENT")
                .param("from", "5")
                .param("size", "5")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1)).getBookingsOnUserItemsByState(
                BookingSearchState.CURRENT, 9L, 5, 5);
    }
}