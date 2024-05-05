package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoPermissionException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        userService.checkIfUserExists(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.getUserById(userId));

        return ItemMapper.toItemDto(itemRepository.addItem(item));
    }

    @Override
    public ItemDto editItem(Long itemId, Long userId, ItemDto itemDto) {
        userService.checkIfUserExists(userId);
        checkIfItemExists(itemId);
        checkIfUserIsOwner(itemId, userId);

        Item item = ItemMapper.toItem(itemDto);

        if (item.getName() == null) {
            item.setName(itemRepository.getItemById(itemId).getName());
        }

        if (item.getDescription() == null) {
            item.setDescription(itemRepository.getItemById(itemId).getDescription());
        }

        if (item.getAvailable() == null) {
            item.setAvailable(itemRepository.getItemById(itemId).getAvailable());
        }

        if (item.getRequest() == null) {
            item.setRequest(itemRepository.getItemById(itemId).getRequest());
        }

        item.setId(itemId);
        item.setOwner(itemRepository.getItemById(itemId).getOwner());

        return ItemMapper.toItemDto(itemRepository.editItem(itemId, item));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        checkIfItemExists(itemId);

        return ItemMapper.toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Long userId) {
        userService.checkIfUserExists(userId);

        List<Item> usersItems = itemRepository.getAllItemsByOwner(userId);
        List<ItemDto> itemsDto = new ArrayList<>();

        if (!usersItems.isEmpty()) {
            for (Item item : usersItems) {
                itemsDto.add(ItemMapper.toItemDto(item));
            }
        }

        return itemsDto;
    }

    @Override
    public List<ItemDto> searchItems(String text, Long userId) {
        userService.checkIfUserExists(userId);

        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }

        List<Item> searchResults = itemRepository.searchItems(text, userId);
        List<ItemDto> searchResultsDto = new ArrayList<>();

        if (!searchResults.isEmpty()) {
            for (Item item : searchResults) {
                searchResultsDto.add(ItemMapper.toItemDto(item));
            }
        }

        return searchResultsDto;
    }

    private void checkIfItemExists(Long itemId) {
        if (!itemRepository.isItemExists(itemId)) {
            throw new NotFoundException(String.format("Item with id=%d not found", itemId));
        }
    }

    private void checkIfUserIsOwner(Long itemId, Long userId) {
        if (!itemRepository.getItemById(itemId).getOwner().getId().equals(userId)) {
            throw new NoPermissionException(String.format("User id=%d does not have permission to edit item id=%d",
                    userId, itemId));
        }
    }
}