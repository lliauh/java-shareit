package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NoPermissionException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper bookingMapper;

    @Override
    public BookingOutDto bookItem(Long userId, BookingDto bookingDto) {
        userService.checkIfUserExists(userId);

        Booking booking = bookingMapper.toBooking(bookingDto);
        booking.setBooker(userRepository.getReferenceById(userId));
        booking.setStatus(BookingStatus.WAITING);

        checkIfBookingIsValid(booking);

        return BookingMapper.toBookingOutDto(bookingRepository.save(booking));
    }

    @Override
    public BookingOutDto processRequest(Long bookingId, Boolean approved, Long userId) {
        checkIfBookingExist(bookingId);
        checkIfBookingStatusIsWaiting(bookingId);
        userService.checkIfUserExists(userId);
        checkIfUserIsTheOwnerOfItem(bookingId, userId);

        Booking booking = bookingRepository.getReferenceById(bookingId);

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        }

        if (!approved) {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.toBookingOutDto(bookingRepository.save(booking));
    }

    @Override
    public BookingOutDto getBookingById(Long bookingId, Long userId) {
        checkIfBookingExist(bookingId);
        userService.checkIfUserExists(userId);

        boolean isOwnerOfItem = bookingRepository.getReferenceById(bookingId).getItem().getOwner().getId()
                .equals(userId);
        boolean isBooker = bookingRepository.getReferenceById(bookingId).getBooker().getId().equals(userId);

        if (!isOwnerOfItem && !isBooker) {
            throw new NoPermissionException(String.format("User id=%d doesn't have permission to get " +
                    "booking id=%d", userId, bookingId));
        }

        return BookingMapper.toBookingOutDto(bookingRepository.getReferenceById(bookingId));
    }

    @Override
    public List<BookingOutDto> getUserBookingsByState(String state, Long userId) {
        userService.checkIfUserExists(userId);

        switch (state) {
            case "ALL":
                return bookingRepository.getAllUserBookings(userId).stream()
                        .map(BookingMapper::toBookingOutDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.getCurrentUserBookings(userId).stream()
                        .map(BookingMapper::toBookingOutDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.getPastUserBookings(userId).stream()
                        .map(BookingMapper::toBookingOutDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.getFutureUserBookings(userId).stream()
                        .map(BookingMapper::toBookingOutDto)
                        .collect(Collectors.toList());
            case "WAITING":
                return bookingRepository.getUserBookingsWithStatusWaiting(userId).stream()
                        .map(BookingMapper::toBookingOutDto)
                        .collect(Collectors.toList());
            case "REJECTED":
                return bookingRepository.getUserBookingsWithStatusRejected(userId).stream()
                        .map(BookingMapper::toBookingOutDto)
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingOutDto> getBookingsOnUserItemsByState(String state, Long userId) {
        userService.checkIfUserExists(userId);

        switch (state) {
            case "ALL":
                return bookingRepository.getAllOwnerBookings(userId).stream()
                        .map(BookingMapper::toBookingOutDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.getCurrentOwnerBookings(userId).stream()
                        .map(BookingMapper::toBookingOutDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.getPastOwnerBookings(userId).stream()
                        .map(BookingMapper::toBookingOutDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.getFutureOwnerBookings(userId).stream()
                        .map(BookingMapper::toBookingOutDto)
                        .collect(Collectors.toList());
            case "WAITING":
                return bookingRepository.getOwnerBookingsWithStatusWaiting(userId).stream()
                        .map(BookingMapper::toBookingOutDto)
                        .collect(Collectors.toList());
            case "REJECTED":
                return bookingRepository.getOwnerBookingsWithStatusRejected(userId).stream()
                        .map(BookingMapper::toBookingOutDto)
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private void checkIfBookingIsValid(Booking booking) {
        itemService.checkIfItemExists(booking.getItem().getId());

        if (!itemService.getItemById(booking.getItem().getId(), booking.getBooker().getId()).getAvailable()) {
            throw new ValidationException(String.format("Item id=%d is not available for booking",
                    booking.getItem().getId()));
        }

        if (itemRepository.getReferenceById(booking.getItem().getId()).getOwner().getId()
                .equals(booking.getBooker().getId())) {
            throw new NoPermissionException(String.format("User id=%d can't book his own item id=%d",
                    booking.getBooker().getId(), booking.getItem().getId()));
        }

        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Start date of the booking can't be in the past");
        }

        if (!booking.getEnd().isAfter(booking.getStart())) {
            throw new ValidationException("End date of the booking should be after start date");
        }
    }

    private void checkIfBookingExist(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException(String.format("Booking with id=%d not found", bookingId));
        }
    }

    private void checkIfBookingStatusIsWaiting(Long bookingId) {
        if (!bookingRepository.getReferenceById(bookingId).getStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException(String.format("Booking id=%d is not ready for approve", bookingId));
        }
    }

    private void checkIfUserIsTheOwnerOfItem(Long bookingId, Long userId) {
        if (!bookingRepository.getReferenceById(bookingId).getItem().getOwner().getId().equals(userId)) {
            throw new NoPermissionException(String.format("User id=%d is not the owner of the item", userId));
        }
    }
}