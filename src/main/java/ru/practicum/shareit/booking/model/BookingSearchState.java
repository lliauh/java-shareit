package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.ValidationException;

public enum BookingSearchState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static void checkSearchQueryState(String state) {
        for (BookingSearchState enumState : BookingSearchState.values()) {
            if (enumState.name().equals(state)) {
                return;
            }
        }

        throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
    }
}