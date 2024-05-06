package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItemsByOwner(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String text, Long userId) {
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public Boolean isItemExists(Long itemId) {
        return items.containsKey(itemId);
    }
}