package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.BookingSearchState;
import ru.practicum.shareit.exception.ValidationException;

import javax.validation.Valid;
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
        log.info("User id={} is changing status of booking id={}, approved state: {}", userId,bookingId, approved);
        return bookingService.processRequest(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingOutDto getBookingById(@PathVariable Long bookingId, @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("User id={} is trying to get booking id={}", userId, bookingId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingOutDto> getUserBookingsByState(@RequestParam(defaultValue = "ALL") String state,
                                                  @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("User id={} is trying to get his bookings in state={}", userId, state);

        checkSearchQueryState(state);
        BookingSearchState searchState = BookingSearchState.valueOf(state);

        return bookingService.getUserBookingsByState(searchState, userId);
    }

    @GetMapping("/owner")
    public List<BookingOutDto> getBookingsOnUserItemsByState(@RequestParam(defaultValue = "ALL")
                                                                 String state,
                                                             @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("User id={} is trying to get bookings on his items in state={}", userId, state);

        checkSearchQueryState(state);
        BookingSearchState searchState = BookingSearchState.valueOf(state);

        return bookingService.getBookingsOnUserItemsByState(searchState, userId);
    }

    private void checkSearchQueryState(String state) {
        for (BookingSearchState enumState : BookingSearchState.values()) {
            if (enumState.name().equals(state)) {
                return;
            }
        }

        throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
    }
}