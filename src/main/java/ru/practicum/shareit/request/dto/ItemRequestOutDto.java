package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemCreatedForRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestOutDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemCreatedForRequestDto> items;

    public ItemRequestOutDto(Long id, String description, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.created = created;
    }
}
