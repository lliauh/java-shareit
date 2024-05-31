package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.BookingSearchState;
import ru.practicum.shareit.exception.ValidationException;

public class BookingSearchStateTest {

    @Test
    void checkState() {
        Assertions.assertDoesNotThrow(() -> {
            BookingSearchState.checkSearchQueryState("ALL");
        });
        Assertions.assertDoesNotThrow(() -> {
            BookingSearchState.checkSearchQueryState("CURRENT");
        });
        Assertions.assertDoesNotThrow(() -> {
            BookingSearchState.checkSearchQueryState("PAST");
        });
        Assertions.assertDoesNotThrow(() -> {
            BookingSearchState.checkSearchQueryState("FUTURE");
        });
        Assertions.assertDoesNotThrow(() -> {
            BookingSearchState.checkSearchQueryState("WAITING");
        });
        Assertions.assertDoesNotThrow(() -> {
            BookingSearchState.checkSearchQueryState("REJECTED");
        });

        Exception exception = Assertions.assertThrows(ValidationException.class, () -> {
            BookingSearchState.checkSearchQueryState("test");
        });

        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
    }
}