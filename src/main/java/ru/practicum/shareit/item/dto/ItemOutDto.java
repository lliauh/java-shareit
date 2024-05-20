package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemOutDto extends ItemDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private BookingEntity lastBooking;
    private BookingEntity nextBooking;
    private List<CommentDto> comments;

    public ItemOutDto(Long id, String name, String description, Boolean available) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
