package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto editItem(Long itemId, Long userId, ItemDto itemDto);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getAllItemsByOwner(Long userId, Integer from, Integer size);

    List<ItemDto> searchItems(String searchQuery, Long userId, Integer from, Integer size);

    void checkIfItemExists(Long itemId);

    CommentDto addComment(Long itemId, Long userId, CommentDto commentDto);
}
