package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> requestItem(@RequestHeader(USER_ID_HEADER) Long userId,
                                              @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Creating new item request from user id={}, request details: {}", userId, itemRequestDto);
        return itemRequestClient.requestItem(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("User id={} is trying to get all his requests", userId);
        return itemRequestClient.getAllUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(USER_ID_HEADER) Long userId,
                                                  @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        log.info("User id={} is trying to get all requests, page starts from={}, size={}", userId, from, size);

        return itemRequestClient.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long requestId) {
        log.info("User id={} is trying to get request id={}", userId, requestId);
        return itemRequestClient.getRequestById(userId, requestId);
    }
}