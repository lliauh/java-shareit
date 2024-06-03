package ru.practicum.shareit.request.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemCreatedForRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@JsonTest
public class ItemRequestOutDtoTest {
    @Autowired
    private JacksonTester<ItemRequestOutDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemCreatedForRequestDto item = new ItemCreatedForRequestDto(1L, "Test item",
                "Test description", 5L, true);
        List<ItemCreatedForRequestDto> items = List.of(item);
        ItemRequestOutDto dto = new ItemRequestOutDto(1L, "Test description", LocalDateTime.now(), items);

        JsonContent<ItemRequestOutDto> result = json.write(dto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Test description");
        Assertions.assertThat(result).extractingJsonPathValue("$.created").isNotNull();
        Assertions.assertThat(result).extractingJsonPathValue("$.items").isNotNull();
    }
}