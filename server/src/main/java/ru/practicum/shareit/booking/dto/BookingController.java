package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.BookingSearchState;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingOutDto bookItem(@RequestHeader(USER_ID_HEADER) Long userId,
                                  @RequestBody @Valid BookingDto bookingDto) {
        log.info("Creating new item request from user id={}, booking details: {}", userId, bookingDto);
        return bookingService.bookItem(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutDto processRequest(@PathVariable Long bookingId, @RequestParam @NotNull Boolean approved,
                                        @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("User id={} is changing status of booking id={}, approved state: {}", userId, bookingId, approved);
        return bookingService.processRequest(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingOutDto getBookingById(@PathVariable Long bookingId, @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("User id={} is trying to get booking id={}", userId, bookingId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingOutDto> getUserBookingsByState(@RequestParam(defaultValue = "ALL") String state,
                                                      @RequestHeader(USER_ID_HEADER) Long userId,
                                                      @RequestParam(required = false, defaultValue = "0")
                                                      @Min(0) Integer from,
                                                      @RequestParam(required = false, defaultValue = "10")
                                                      @Min(1) Integer size) {
        log.info("User id={} is trying to get his bookings in state={}, page starts from={}, size={}", userId, state,
                from, size);

        return bookingService.getUserBookingsByState(BookingSearchState.valueOf(state), userId, from, size);
    }

    @GetMapping("/owner")
    public List<BookingOutDto> getBookingsOnUserItemsByState(@RequestParam(defaultValue = "ALL")
                                                             String state,
                                                             @RequestHeader(USER_ID_HEADER) Long userId,
                                                             @RequestParam(required = false, defaultValue = "0")
                                                             @Min(0) Integer from,
                                                             @RequestParam(required = false, defaultValue = "10")
                                                             @Min(1) Integer size) {
        log.info("User id={} is trying to get bookings on his items in state={}, page starts from={}, size={}", userId,
                state, from, size);

        return bookingService.getBookingsOnUserItemsByState(BookingSearchState.valueOf(state), userId, from, size);
    }
}