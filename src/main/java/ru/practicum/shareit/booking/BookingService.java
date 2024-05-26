package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.BookingSearchState;

import java.util.List;

public interface BookingService {
    BookingOutDto bookItem(Long userId, BookingDto bookingDto);

    BookingOutDto processRequest(Long bookingId, Boolean approved, Long userId);

    BookingOutDto getBookingById(Long bookingId, Long userId);

    List<BookingOutDto> getUserBookingsByState(BookingSearchState state, Long userId, Integer from, Integer size);

    List<BookingOutDto> getBookingsOnUserItemsByState(BookingSearchState state, Long userId, Integer from, Integer size);
}