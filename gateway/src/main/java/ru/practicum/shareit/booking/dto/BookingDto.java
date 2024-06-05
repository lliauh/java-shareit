package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.exception.ValidationException;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    @NotNull
    private Long itemId;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;

    public BookingDto(Long id, Long itemId, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }

    public static void validateBooking(BookingDto booking) {
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Start date of the booking can't be in the past");
        }

        if (!booking.getEnd().isAfter(booking.getStart())) {
            throw new ValidationException("End date of the booking should be after start date");
        }
    }
}