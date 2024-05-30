package ru.practicum.shareit.booking.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@JsonTest
public class BookingOutDtoTest {
    @Autowired
    private JacksonTester<BookingOutDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemDto item = new ItemDto(12L, "Item1", "Test_description", true);
        UserDto booker  = new UserDto(7L, "TestName" ,"test@mail.ts");
        BookingOutDto dto = new BookingOutDto(10L, LocalDateTime.now(), LocalDateTime.now(), item, booker,
                BookingStatus.WAITING);

        JsonContent<BookingOutDto> result = json.write(dto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(10);
        Assertions.assertThat(result).extractingJsonPathValue("$.start").isNotNull();
        Assertions.assertThat(result).extractingJsonPathValue("$.end").isNotNull();
        Assertions.assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(12);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("Item1");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo("Test_description");
        Assertions.assertThat(result).extractingJsonPathBooleanValue("$.item.available").isTrue();
        Assertions.assertThat(result).extractingJsonPathValue("$.booker").isNotNull();
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(7);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.booker.name")
                .isEqualTo("TestName");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.booker.email")
                .isEqualTo("test@mail.ts");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }
}