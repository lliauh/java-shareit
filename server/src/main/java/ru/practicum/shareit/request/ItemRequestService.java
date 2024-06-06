package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto requestItem(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestOutDto> getAllUserRequests(Long userId);

    List<ItemRequestOutDto> getAllRequests(Long userId, Integer from, Integer size);

    ItemRequestOutDto getRequestById(Long userId, Long requestId);
}
