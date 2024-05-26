package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(),
                itemRequest.getCreated());
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(itemRequestDto.getDescription());
    }

    public static ItemRequestOutDto toItemRequestOutDto(ItemRequest itemRequest) {
        return new ItemRequestOutDto(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getCreated());
    }
}
