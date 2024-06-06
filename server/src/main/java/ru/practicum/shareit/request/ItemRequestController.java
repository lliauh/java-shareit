package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto requestItem(@RequestHeader(USER_ID_HEADER) Long userId,
                                      @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Creating new item request from user id={}, request details: {}", userId, itemRequestDto);
        return itemRequestService.requestItem(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestOutDto> getAllUserRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("User id={} is trying to get all his requests", userId);
        return itemRequestService.getAllUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestOutDto> getAllRequests(@RequestHeader(USER_ID_HEADER) Long userId,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("User id={} is trying to get all requests, page starts from={}, size={}", userId, from, size);

        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutDto getRequestById(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long requestId) {
        log.info("User id={} is trying to get request id={}", userId, requestId);
        return itemRequestService.getRequestById(userId, requestId);
    }
}