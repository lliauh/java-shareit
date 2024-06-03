package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class ItemMapperTest {

    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setId(1L);
        item.setName("Test item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(new User("Test user", "mail@test.ts"));

        itemDto = new ItemDto(2L, "Test itemDto", "Test decription itemDto", false);
    }

    @Test
    void toItemDtoTest() {
        ItemDto result = ItemMapper.toItemDto(item);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), item.getId());
        Assertions.assertEquals(result.getName(), item.getName());
        Assertions.assertEquals(result.getDescription(), item.getDescription());
        Assertions.assertEquals(result.getAvailable(), item.getAvailable());
    }

    @Test
    void toItemOutDtoTest() {
        ItemOutDto result = ItemMapper.toItemOutDto(item);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), item.getId());
        Assertions.assertEquals(result.getName(), item.getName());
        Assertions.assertEquals(result.getDescription(), item.getDescription());
        Assertions.assertEquals(result.getAvailable(), item.getAvailable());
    }

    @Test
    void toItemTest() {
        Item result = ItemMapper.toItem(itemDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getName(), itemDto.getName());
        Assertions.assertEquals(result.getDescription(), itemDto.getDescription());
        Assertions.assertEquals(result.getAvailable(), itemDto.getAvailable());
    }

    @Test
    void toItemCreatedForRequestDtoTest() {
        ItemRequest request = new ItemRequest();
        request.setId(5L);
        request.setDescription("Test request");
        request.setRequester(new User("Test user2", "mail2@test.ts"));
        request.setCreated(LocalDateTime.now());

        item.setRequest(request);

        ItemCreatedForRequestDto result = ItemMapper.toItemCreatedForRequestDto(item);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), item.getId());
        Assertions.assertEquals(result.getName(), item.getName());
        Assertions.assertEquals(result.getDescription(), item.getDescription());
        Assertions.assertEquals(result.getAvailable(), item.getAvailable());
        Assertions.assertEquals(result.getRequestId(), item.getRequest().getId());
    }
}