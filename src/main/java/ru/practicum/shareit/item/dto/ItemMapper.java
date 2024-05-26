package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );

        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }

        return itemDto;
    }

    public static ItemOutDto toItemOutDto(Item item) {
        ItemOutDto itemOutDto = new ItemOutDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );

        if (item.getRequest() != null) {
            itemOutDto.setRequestId(item.getRequest().getId());
        }

        return itemOutDto;
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
    }

    public static ItemCreatedForRequestDto toItemCreatedForRequestDto(Item item) {
        return new ItemCreatedForRequestDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getRequest().getId(),
                item.getAvailable()
        );
    }
}