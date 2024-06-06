package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class ItemRequestMapperTest {

    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Test request");
        itemRequest.setRequester(new User("Test user", "mail@test.ts"));
        itemRequest.setCreated(LocalDateTime.now());

        itemRequestDto = new ItemRequestDto(2L, "Test request 2", LocalDateTime.now());
    }


    @Test
    void toItemRequestDtoTest() {
        ItemRequestDto result = ItemRequestMapper.toItemRequestDto(itemRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), itemRequest.getId());
        Assertions.assertEquals(result.getDescription(), itemRequest.getDescription());
        Assertions.assertEquals(result.getCreated(), itemRequest.getCreated());
    }

    @Test
    void toItemRequestTest() {
        ItemRequest result = ItemRequestMapper.toItemRequest(itemRequestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getDescription(), itemRequestDto.getDescription());
    }

    @Test
    void toItemRequestOutDtoTest() {
        ItemRequestOutDto result = ItemRequestMapper.toItemRequestOutDto(itemRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), itemRequest.getId());
        Assertions.assertEquals(result.getDescription(), itemRequest.getDescription());
        Assertions.assertEquals(result.getCreated(), itemRequest.getCreated());
    }
}