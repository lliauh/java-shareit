package ru.practicum.shareit.booking.dto;

import lombok.Data;

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
}