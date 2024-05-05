package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long lastItemId = 0L;

    @Override
    public Item addItem(Item item) {
        lastItemId++;
        item.setId(lastItemId);

        items.put(item.getId(), item);

        return items.get(item.getId());
    }

    @Override
    public Item editItem(Long itemId, Item item) {
        items.put(itemId, item);

        return items.get(itemId);
    }

    @Override
    public Item getItemById(Long itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        }

        return null;
    }

    @Override
    public List<Item> getAllItemsByOwner(Long userId) {
        List<Item> usersItems = new ArrayList<>();

        for (Item item : items.values()) {
            if (item.getOwner().getId() == userId) {
                usersItems.add(item);
            }
        }

        return usersItems;
    }

    @Override
    public List<Item> searchItems(String text, Long userId) {
        List<Item> searchResults = new ArrayList<>();

        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase())
                    || item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.getAvailable()) {
                    searchResults.add(item);
                }
            }

        return searchResults;
    }

    @Override
    public Boolean isItemExists(Long itemId) {
        return items.containsKey(itemId);
    }
}
