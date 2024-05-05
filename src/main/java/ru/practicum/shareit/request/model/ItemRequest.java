package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@Data
public class ItemRequest {
    Long id;
    String description;
    User requester;
    LocalDateTime created;
}
