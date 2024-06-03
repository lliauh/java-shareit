package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemCreatedForRequestDto {
    private Long id;
    private String name;
    private String description;
    private Long requestId;
    private Boolean available;
}
