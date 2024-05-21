package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final ItemRepository itemRepository;

    public Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(itemRepository.getReferenceById(bookingDto.getItemId()));

        return booking;
    }

    public static BookingOutDto toBookingOutDto(Booking booking) {
        BookingOutDto bookingOutDto = new BookingOutDto();

        bookingOutDto.setId(booking.getId());
        bookingOutDto.setStart(booking.getStart());
        bookingOutDto.setEnd(booking.getEnd());
        bookingOutDto.setItem(ItemMapper.toItemDto(booking.getItem()));
        bookingOutDto.setBooker(UserMapper.toUserDto(booking.getBooker()));
        bookingOutDto.setStatus(booking.getStatus());

        return bookingOutDto;
    }
}