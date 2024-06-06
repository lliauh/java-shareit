package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ShareItServerTest {

    @Test
    void testMain() {
        Assertions.assertDoesNotThrow(ShareItServer::new);
        Assertions.assertDoesNotThrow(() -> ShareItServer.main(new String[]{}));
    }
}
