package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapperTest {

    @Test
    void toCommentTest() {
        CommentDto dto = new CommentDto();
        dto.setText("Test comment");

        Comment comment = CommentMapper.toComment(dto);

        Assertions.assertNotNull(comment);
        Assertions.assertEquals(dto.getText(), comment.getText());
    }

    @Test
    void toCommentDtoTest() {
        User author = new User("Author", "mail@test.ts");
        Item item = new Item("Name", "Description", true, author);

        Comment comment = new Comment("Test comment");
        comment.setId(1L);
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        CommentDto commentDto = CommentMapper.toCommentDto(comment);

        Assertions.assertNotNull(commentDto);
        Assertions.assertEquals(comment.getId(), commentDto.getId());
        Assertions.assertEquals(comment.getText(), commentDto.getText());
        Assertions.assertEquals(comment.getAuthor().getName(), commentDto.getAuthorName());
        Assertions.assertEquals(comment.getCreated(), commentDto.getCreated());
    }
}