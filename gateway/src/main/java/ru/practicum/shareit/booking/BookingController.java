package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSearchState;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingClient bookingClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader(USER_ID_HEADER) Long userId,
                                           @RequestBody @Valid BookingDto bookingDto) {
        log.info("Creating new item request from user id={}, booking details: {}", userId, bookingDto);

        BookingDto.validateBooking(bookingDto);

        return bookingClient.bookItem(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> processRequest(@PathVariable Long bookingId, @RequestParam @NotNull Boolean approved,
                                     @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("User id={} is changing status of booking id={}, approved state: {}", userId, bookingId, approved);
        return bookingClient.processRequest(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable Long bookingId, @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("User id={} is trying to get booking id={}", userId, bookingId);
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookingsByState(@RequestParam(defaultValue = "ALL") String state,
                                                      @RequestHeader(USER_ID_HEADER) Long userId,
                                                      @RequestParam(required = false, defaultValue = "0")
                                                          @Min(0) Integer from,
                                                      @RequestParam(required = false, defaultValue = "10")
                                                          @Min(1) Integer size) {
        log.info("User id={} is trying to get his bookings in state={}, page starts from={}, size={}", userId, state,
                from, size);

        BookingSearchState.checkSearchQueryState(state);

        return bookingClient.getUserBookings(userId, BookingSearchState.valueOf(state), from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsOnUserItemsByState(@RequestParam(defaultValue = "ALL")
                                                                 String state,
                                                             @RequestHeader(USER_ID_HEADER) Long userId,
                                                             @RequestParam(required = false, defaultValue = "0")
                                                                 @Min(0) Integer from,
                                                             @RequestParam(required = false, defaultValue = "10")
                                                                 @Min(1) Integer size) {
        log.info("User id={} is trying to get bookings on his items in state={}, page starts from={}, size={}", userId,
                state, from, size);

        BookingSearchState.checkSearchQueryState(state);

        return bookingClient.getOwnerBookings(userId, BookingSearchState.valueOf(state), from, size);
    }
}