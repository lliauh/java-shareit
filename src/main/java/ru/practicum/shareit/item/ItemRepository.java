package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item addItem(Item item);

    Item editItem(Long itemId, Item item);

    Item getItemById(Long itemId);

    List<Item> getAllItemsByOwner(Long userId);

    List<Item> searchItems(String text, Long userId);

    Boolean isItemExists(Long itemId);
}
