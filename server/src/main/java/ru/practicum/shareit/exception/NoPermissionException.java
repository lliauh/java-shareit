package ru.practicum.shareit.exception;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException(final String message) {
        super(message);
    }
}
