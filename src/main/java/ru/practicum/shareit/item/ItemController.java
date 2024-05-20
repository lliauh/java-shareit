package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addItem(@RequestHeader(USER_ID_HEADER) Long userId, @RequestBody @Valid ItemDto itemDto) {
        log.info("Creating new item={} by user id={}", itemDto, userId);
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@PathVariable Long itemId,
                            @RequestHeader(USER_ID_HEADER) Long userId,
                            @RequestBody ItemDto itemDto) {
        log.info("Updating item id={} by user id={}, updated item={}", itemId, userId, itemDto);
        return itemService.editItem(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Getting item id={} by user id={}", itemId, userId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Getting all items by user id={}", userId);
        return itemService.getAllItemsByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text, @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Searching available items by user id={}, search query={}", userId, text);
        return itemService.searchItems(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId, @RequestHeader(USER_ID_HEADER) Long userId,
                                 @RequestBody @Valid CommentDto commentDto) {
        log.info("User id={} is trying to add comment to item id={}", userId, itemId);
        return itemService.addComment(itemId, userId, commentDto);
    }
}