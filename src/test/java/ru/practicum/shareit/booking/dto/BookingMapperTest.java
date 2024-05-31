package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class BookingMapperTest {

    @Test
    void toBooking() {
        BookingDto bookingDto = new BookingDto(null, 3L, LocalDateTime.now(), LocalDateTime.now());

        Booking booking = BookingMapper.toBooking(bookingDto);

        Assertions.assertNotNull(booking);
        Assertions.assertEquals(bookingDto.getStart(), booking.getStart());
        Assertions.assertEquals(bookingDto.getEnd(), booking.getEnd());
    }

    @Test
    void toBookingOutDto() {
        Item item = new Item("Test item", "Test description", true);
        User booker = new User("Test user", "test@test.ts");

        Booking booking = new Booking();
        booking.setId(2L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);

        BookingOutDto bookingOutDto = BookingMapper.toBookingOutDto(booking);

        Assertions.assertNotNull(bookingOutDto);
        Assertions.assertEquals(booking.getId(), bookingOutDto.getId());
        Assertions.assertEquals(booking.getStart(), bookingOutDto.getStart());
        Assertions.assertEquals(booking.getEnd(), bookingOutDto.getEnd());
        Assertions.assertEquals(booking.getItem().getName(), bookingOutDto.getItem().getName());
        Assertions.assertEquals(booking.getItem().getDescription(), bookingOutDto.getItem().getDescription());
        Assertions.assertEquals(booking.getItem().getAvailable(), bookingOutDto.getItem().getAvailable());
        Assertions.assertEquals(booking.getBooker().getName(), bookingOutDto.getBooker().getName());
        Assertions.assertEquals(booking.getBooker().getEmail(), bookingOutDto.getBooker().getEmail());
        Assertions.assertEquals(booking.getStatus(), bookingOutDto.getStatus());
    }
}
