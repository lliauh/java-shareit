package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;

    public BookingDto(Long id, Long itemId, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}