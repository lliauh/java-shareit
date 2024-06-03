package ru.practicum.shareit.item.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
public class ItemCreatedForRequestDtoTest {
    @Autowired
    private JacksonTester<ItemCreatedForRequestDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemCreatedForRequestDto dto = new ItemCreatedForRequestDto(1L, "Test item",
                "Test description", 5L, true);

        JsonContent<ItemCreatedForRequestDto> result = json.write(dto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("Test item");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Test description");
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(5);
        Assertions.assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
    }
}