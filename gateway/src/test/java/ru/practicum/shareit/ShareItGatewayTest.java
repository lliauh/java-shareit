package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ShareItGatewayTest {

    @Test
    void testMain() {
        Assertions.assertDoesNotThrow(ShareItGateway::new);
        Assertions.assertDoesNotThrow(() -> ShareItGateway.main(new String[]{}));
    }
}
