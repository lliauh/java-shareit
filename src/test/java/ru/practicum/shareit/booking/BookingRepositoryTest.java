package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Item item;
    private User user;

    @BeforeEach
    void setUp() {
        this.user = createUser("Leo", "leo@leo.le");
        User owner = createUser("Owner", "best@owner.rs");
        this.item = createItem("Item1","Item_description", true, owner);
    }

    private User createUser(String name, String email) {
        User user = new User(name, email);
        return userRepository.save(user);
    }

    private Item createItem(String name, String description, Boolean available, User owner) {
        Item item = new Item(name, description, available, owner);
        return itemRepository.save(item);
    }

    private Booking createBooking(BookingStatus status, Item item, User booker, LocalDateTime start,
                                  LocalDateTime end) {
        Booking booking = new Booking();
        booking.setStatus(status);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(start);
        booking.setEnd(end);

        return bookingRepository.save(booking);
    }

    @Test
    void testGetAllUserBookings() {
        Booking booking = createBooking(BookingStatus.APPROVED, item, user, LocalDateTime.now(), LocalDateTime.now());
        List<Booking> result = bookingRepository.getAllUserBookings(user.getId(), PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(booking.getId(), result.get(0).getId());
        Assertions.assertEquals(booking.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(booking.getItem(), result.get(0).getItem());
        Assertions.assertEquals(booking.getBooker(), result.get(0).getBooker());
    }

    @Test
    void testGetCurrentUserBookings() {
        Booking booking = createBooking(BookingStatus.APPROVED, item, user, LocalDateTime.now(),
                LocalDateTime.now().plusDays(5));
        List<Booking> result = bookingRepository.getCurrentUserBookings(user.getId(), PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(booking.getId(), result.get(0).getId());
        Assertions.assertEquals(booking.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(booking.getItem(), result.get(0).getItem());
        Assertions.assertEquals(booking.getBooker(), result.get(0).getBooker());
    }

    @Test
    void testGetPastUserBookings() {
        Booking booking = createBooking(BookingStatus.APPROVED, item, user, LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(5));
        List<Booking> result = bookingRepository.getPastUserBookings(user.getId(), PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(booking.getId(), result.get(0).getId());
        Assertions.assertEquals(booking.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(booking.getItem(), result.get(0).getItem());
        Assertions.assertEquals(booking.getBooker(), result.get(0).getBooker());
    }

    @Test
    void testGetFutureUserBookings() {
        Booking booking = createBooking(BookingStatus.APPROVED, item, user, LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(10));
        List<Booking> result = bookingRepository.getFutureUserBookings(user.getId(), PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(booking.getId(), result.get(0).getId());
        Assertions.assertEquals(booking.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(booking.getItem(), result.get(0).getItem());
        Assertions.assertEquals(booking.getBooker(), result.get(0).getBooker());
    }

    @Test
    void testGetUserBookingsWithStatusWaiting() {
        Booking booking = createBooking(BookingStatus.WAITING, item, user, LocalDateTime.now(), LocalDateTime.now());
        List<Booking> result = bookingRepository.getUserBookingsWithStatusWaiting(user.getId(),
                PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(booking.getId(), result.get(0).getId());
        Assertions.assertEquals(booking.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(booking.getItem(), result.get(0).getItem());
        Assertions.assertEquals(booking.getBooker(), result.get(0).getBooker());
    }

    @Test
    void testGetUserBookingsWithStatusRejected() {
        Booking booking = createBooking(BookingStatus.REJECTED, item, user, LocalDateTime.now(), LocalDateTime.now());
        List<Booking> result = bookingRepository.getUserBookingsWithStatusRejected(user.getId(),
                PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(booking.getId(), result.get(0).getId());
        Assertions.assertEquals(booking.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(booking.getItem(), result.get(0).getItem());
        Assertions.assertEquals(booking.getBooker(), result.get(0).getBooker());
    }

    @Test
    void testGetAllOwnerBookings() {
        Booking booking = createBooking(BookingStatus.WAITING, item, user, LocalDateTime.now(), LocalDateTime.now());
        List<Booking> result = bookingRepository.getAllOwnerBookings(item.getOwner().getId(),
                PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(booking.getId(), result.get(0).getId());
        Assertions.assertEquals(booking.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(booking.getItem(), result.get(0).getItem());
        Assertions.assertEquals(booking.getBooker(), result.get(0).getBooker());
    }

    @Test
    void testGetCurrentOwnerBookings() {
        Booking booking = createBooking(BookingStatus.APPROVED, item, user, LocalDateTime.now(),
                LocalDateTime.now().plusDays(5));
        List<Booking> result = bookingRepository.getCurrentOwnerBookings(item.getOwner().getId(),
                PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(booking.getId(), result.get(0).getId());
        Assertions.assertEquals(booking.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(booking.getItem(), result.get(0).getItem());
        Assertions.assertEquals(booking.getBooker(), result.get(0).getBooker());
    }

    @Test
    void testGetPastOwnerBookings() {
        Booking booking = createBooking(BookingStatus.APPROVED, item, user, LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(5));
        List<Booking> result = bookingRepository.getPastOwnerBookings(item.getOwner().getId(),
                PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(booking.getId(), result.get(0).getId());
        Assertions.assertEquals(booking.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(booking.getItem(), result.get(0).getItem());
        Assertions.assertEquals(booking.getBooker(), result.get(0).getBooker());
    }

    @Test
    void testGetFutureOwnerBookings() {
        Booking booking = createBooking(BookingStatus.APPROVED, item, user, LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(10));
        List<Booking> result = bookingRepository.getFutureOwnerBookings(item.getOwner().getId(),
                PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(booking.getId(), result.get(0).getId());
        Assertions.assertEquals(booking.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(booking.getItem(), result.get(0).getItem());
        Assertions.assertEquals(booking.getBooker(), result.get(0).getBooker());
    }

    @Test
    void testGetOwnerBookingsWithStatusWaiting() {
        Booking booking = createBooking(BookingStatus.WAITING, item, user, LocalDateTime.now(), LocalDateTime.now());
        List<Booking> result = bookingRepository.getOwnerBookingsWithStatusWaiting(item.getOwner().getId(),
                PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(booking.getId(), result.get(0).getId());
        Assertions.assertEquals(booking.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(booking.getItem(), result.get(0).getItem());
        Assertions.assertEquals(booking.getBooker(), result.get(0).getBooker());
    }

    @Test
    void testGetOwnerBookingsWithStatusRejected() {
        Booking booking = createBooking(BookingStatus.REJECTED, item, user, LocalDateTime.now(), LocalDateTime.now());
        List<Booking> result = bookingRepository.getOwnerBookingsWithStatusRejected(item.getOwner().getId(),
                PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(booking.getId(), result.get(0).getId());
        Assertions.assertEquals(booking.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(booking.getItem(), result.get(0).getItem());
        Assertions.assertEquals(booking.getBooker(), result.get(0).getBooker());
    }

    @Test
    void testGetLastBooking() {
        Booking booking = createBooking(BookingStatus.APPROVED, item, user, LocalDateTime.now().minusDays(5),
                LocalDateTime.now());
        Optional<Booking> result = bookingRepository.getLastBooking(item.getId());

        Assertions.assertNotNull(result.get());
        Assertions.assertEquals(booking.getId(), result.get().getId());
        Assertions.assertEquals(booking.getStatus(), result.get().getStatus());
        Assertions.assertEquals(booking.getItem(), result.get().getItem());
        Assertions.assertEquals(booking.getBooker(), result.get().getBooker());
    }

    @Test
    void testGetNextBooking() {
        Booking booking = createBooking(BookingStatus.APPROVED, item, user, LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(10));
        Optional<Booking> result = bookingRepository.getNextBooking(item.getId());

        Assertions.assertNotNull(result.get());
        Assertions.assertEquals(booking.getId(), result.get().getId());
        Assertions.assertEquals(booking.getStatus(), result.get().getStatus());
        Assertions.assertEquals(booking.getItem(), result.get().getItem());
        Assertions.assertEquals(booking.getBooker(), result.get().getBooker());
    }

    @Test
    void testGetApprovedBookingInThePastByBookerIdAndItemId() {
        Booking booking = createBooking(BookingStatus.APPROVED, item, user, LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(5));
        Optional<Booking> result = bookingRepository.getApprovedBookingInThePastByBookerIdAndItemId(booking
                        .getBooker().getId(), item.getId());

        Assertions.assertNotNull(result.get());
        Assertions.assertEquals(booking.getId(), result.get().getId());
        Assertions.assertEquals(booking.getStatus(), result.get().getStatus());
        Assertions.assertEquals(booking.getItem(), result.get().getItem());
        Assertions.assertEquals(booking.getBooker(), result.get().getBooker());
    }
}