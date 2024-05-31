package ru.practicum.shareit.item.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

@JsonTest
public class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testSerialize() throws Exception {
        CommentDto dto = new CommentDto(1L, "test comment", "Test author", LocalDateTime.now());

        JsonContent<CommentDto> result = json.write(dto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.text")
                .isEqualTo("test comment");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.authorName")
                .isEqualTo("Test author");
        Assertions.assertThat(result).extractingJsonPathValue("$.created").isNotNull();
    }
}