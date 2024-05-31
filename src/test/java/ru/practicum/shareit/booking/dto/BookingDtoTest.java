package ru.practicum.shareit.booking.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

@JsonTest
public class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {
        BookingDto dto = new BookingDto(1L, 2L, LocalDateTime.now(), LocalDateTime.now());

        JsonContent<BookingDto> result = json.write(dto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(2);
        Assertions.assertThat(result).extractingJsonPathValue("$.start").isNotNull();
        Assertions.assertThat(result).extractingJsonPathValue("$.end").isNotNull();
    }
}