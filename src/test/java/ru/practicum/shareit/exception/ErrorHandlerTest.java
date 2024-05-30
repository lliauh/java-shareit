package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ErrorHandlerTest {

    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void handleNotFoundException() {
        NotFoundException exception = new NotFoundException("Not found exception test");
        ErrorResponse result = errorHandler.handleNotFoundException(exception);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(exception.getMessage(), result.getError());
    }

    @Test
    void handleAlreadyExistsException() {
        AlreadyExistsException exception = new AlreadyExistsException("Already exists exception test");
        ErrorResponse result = errorHandler.handleAlreadyExists(exception);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(exception.getMessage(), result.getError());
    }

    @Test
    void handleNoPermissionException() {
        NoPermissionException exception = new NoPermissionException("No permission exception test");
        ErrorResponse result = errorHandler.handleNoPermission(exception);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(exception.getMessage(), result.getError());
    }

    @Test
    void handleValidationException() {
        ValidationException exception = new ValidationException("Validation exception test");
        ErrorResponse result = errorHandler.handleValidationException(exception);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(exception.getMessage(), result.getError());
    }
}