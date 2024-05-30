package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ItemRequestRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestsRepository itemRequestsRepository;

    private User user1;
    private User user2;

    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User("Test1", "mail1@test.ts"));
        user2 = userRepository.save(new User("Test2", "mail2@test.ts"));

        itemRequest1 = new ItemRequest("Test request1");
        itemRequest1.setRequester(user1);
        itemRequest1.setCreated(LocalDateTime.now());
        itemRequest1 = itemRequestsRepository.save(itemRequest1);

        itemRequest2 = new ItemRequest("Test request2");
        itemRequest2.setRequester(user2);
        itemRequest2.setCreated(LocalDateTime.now());
        itemRequest2 = itemRequestsRepository.save(itemRequest2);
    }

    @Test
    void testFindAllByRequesterId() {
        Optional<List<ItemRequest>> result = itemRequestsRepository.findAllByRequesterId(user1.getId());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1, result.get().size());
        Assertions.assertEquals(itemRequest1.getId(), result.get().get(0).getId());
        Assertions.assertEquals(itemRequest1.getDescription(), result.get().get(0).getDescription());
        Assertions.assertEquals(itemRequest1.getCreated(), result.get().get(0).getCreated());
        Assertions.assertEquals(itemRequest1.getRequester(), result.get().get(0).getRequester());
    }
}