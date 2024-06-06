package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User user;
    private Item item;
    private Comment comment;

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
    void findAllByItemIdTest() {
        comment = new Comment("Test comment");
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comment = commentRepository.save(comment);

        Optional<List<Comment>> result = commentRepository.findAllByItemId(item.getId());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1, result.get().size());
        Assertions.assertEquals(comment.getId(), result.get().get(0).getId());
        Assertions.assertEquals(comment.getText(), result.get().get(0).getText());
        Assertions.assertEquals(comment.getItem(), result.get().get(0).getItem());
        Assertions.assertEquals(comment.getCreated(), result.get().get(0).getCreated());
        Assertions.assertEquals(comment.getAuthor(), result.get().get(0).getAuthor());
    }
}