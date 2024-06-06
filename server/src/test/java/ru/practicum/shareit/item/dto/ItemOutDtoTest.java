package ru.practicum.shareit.item.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
public class ItemOutDtoTest {
    @Autowired
    private JacksonTester<ItemOutDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemOutDto dto = new ItemOutDto(1L, "Test item", "Test description", true);

        JsonContent<ItemOutDto> result = json.write(dto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("Test item");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Test description");
        Assertions.assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
    }
}