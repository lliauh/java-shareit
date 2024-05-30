package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestsRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestsRepository itemRequestsRepository;

    private Item item;
    private User user;

    @BeforeEach
    void setUp() {
        this.user = createUser("Leo", "leo@leo.le");
        User owner = createUser("Owner", "best@owner.rs");
        this.item = createItem("Item1","Item_description", true, owner);
    }

    private User createUser(String name, String email) {
        User user = new User(name, email);
        return userRepository.save(user);
    }

    private Item createItem(String name, String description, Boolean available, User owner) {
        Item item = new Item(name, description, available, owner);
        return itemRepository.save(item);
    }

    @Test
    void findAllByOwnerIdTest() {
        List<Item> result = itemRepository.findAllByOwnerId(item.getOwner().getId(), PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(item.getId(), result.get(0).getId());
        Assertions.assertEquals(item.getName(), result.get(0).getName());
        Assertions.assertEquals(item.getDescription(), result.get(0).getDescription());
        Assertions.assertEquals(item.getAvailable(), result.get(0).getAvailable());
        Assertions.assertEquals(item.getOwner(), result.get(0).getOwner());
    }

    @Test
    void searchItemsTest() {
        List<Item> result = itemRepository.searchItems("Item_description", PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(item.getId(), result.get(0).getId());
        Assertions.assertEquals(item.getName(), result.get(0).getName());
        Assertions.assertEquals(item.getDescription(), result.get(0).getDescription());
        Assertions.assertEquals(item.getAvailable(), result.get(0).getAvailable());
        Assertions.assertEquals(item.getOwner(), result.get(0).getOwner());
    }

    @Test
    void findAllByRequestIdTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Test request");
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest = itemRequestsRepository.save(itemRequest);

        item.setRequest(itemRequest);

        Optional<List<Item>> result = itemRepository.findAllByRequestId(item.getRequest().getId());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1, result.get().size());
        Assertions.assertEquals(item.getId(), result.get().get(0).getId());
        Assertions.assertEquals(item.getName(), result.get().get(0).getName());
        Assertions.assertEquals(item.getDescription(), result.get().get(0).getDescription());
        Assertions.assertEquals(item.getAvailable(), result.get().get(0).getAvailable());
        Assertions.assertEquals(item.getOwner(), result.get().get(0).getOwner());
    }
}