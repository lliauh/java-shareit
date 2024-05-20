package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

public interface BookingService {
    BookingOutDto bookItem(Long userId, BookingDto bookingDto);

    BookingOutDto processRequest(Long bookingId, Boolean approved, Long userId);

    BookingOutDto getBookingById(Long bookingId, Long userId);

    List<BookingOutDto> getUserBookingsByState(String state, Long userId);

    List<BookingOutDto> getBookingsOnUserItemsByState(String state, Long userId);
}