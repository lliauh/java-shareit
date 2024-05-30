package ru.practicum.shareit.booking;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    private User user;
    private Booking booking;
    private Item item;
    private ItemDto itemDto;
    private User owner;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test user");
        user.setEmail("test@mail.nl");

        owner = new User();
        owner.setId(2L);
        owner.setName("Owner");
        owner.setEmail("test2@mail.nl");

        item = new Item();
        item.setId(1L);
        item.setName("Test item");
        item.setDescription("Test description");
        item.setAvailable(true);
        item.setOwner(owner);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test item");
        itemDto.setDescription("Test description");
        itemDto.setAvailable(true);

        booking = new Booking();
        booking.setId(9L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(5));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        bookingDto = new BookingDto(9L, item.getId(), booking.getStart(), booking.getEnd());
        bookingDto.setItemId(item.getId());
    }

    @Test
    void testBookItem() {
        when(bookingRepository.save(any())).thenReturn(booking);
        when(itemRepository.getReferenceById(any())).thenReturn(item);
        when(userRepository.getReferenceById(any())).thenReturn(user);
        when(itemService.getItemById(any(), any())).thenReturn(itemDto);


        BookingOutDto result = bookingService.bookItem(user.getId(), bookingDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(9L, result.getId());
        Assertions.assertEquals(booking.getStart(), result.getStart());
        Assertions.assertEquals(booking.getEnd(), result.getEnd());
        Assertions.assertEquals(booking.getStatus(), result.getStatus());
        Assertions.assertEquals(booking.getItem().getId(), result.getItem().getId());
        Assertions.assertEquals(booking.getItem().getName(), result.getItem().getName());
        Assertions.assertEquals(booking.getBooker().getId(), result.getBooker().getId());
        Assertions.assertEquals(booking.getBooker().getName(), result.getBooker().getName());

        verify(itemRepository, times(2)).getReferenceById(any());
        verifyNoMoreInteractions(itemRepository);
        verify(userRepository, times(1)).getReferenceById(any());
        verifyNoMoreInteractions(userRepository);
        verify(bookingRepository, times(1)).save(any());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testProcessRequest() {
        when(bookingRepository.save(any())).thenReturn(booking);
        when(bookingRepository.getReferenceById(any())).thenReturn(booking);
        when(bookingRepository.existsById(any())).thenReturn(true);

        BookingOutDto result = bookingService.processRequest(booking.getId(), true, owner.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(9L, result.getId());
        Assertions.assertEquals(booking.getStart(), result.getStart());
        Assertions.assertEquals(booking.getEnd(), result.getEnd());
        Assertions.assertEquals(BookingStatus.APPROVED, result.getStatus());
        Assertions.assertEquals(booking.getItem().getId(), result.getItem().getId());
        Assertions.assertEquals(booking.getItem().getName(), result.getItem().getName());
        Assertions.assertEquals(booking.getBooker().getId(), result.getBooker().getId());
        Assertions.assertEquals(booking.getBooker().getName(), result.getBooker().getName());

        verify(bookingRepository, times(1)).existsById(any());
        verify(bookingRepository, times(3)).getReferenceById(any());
        verify(bookingRepository, times(1)).save(any());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetBookingById() {
        when(bookingRepository.existsById(any())).thenReturn(true);
        when(bookingRepository.getReferenceById(any())).thenReturn(booking);

        BookingOutDto result = bookingService.getBookingById(booking.getId(), owner.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(9L, result.getId());
        Assertions.assertEquals(booking.getStart(), result.getStart());
        Assertions.assertEquals(booking.getEnd(), result.getEnd());
        Assertions.assertEquals(booking.getStatus(), result.getStatus());
        Assertions.assertEquals(booking.getItem().getId(), result.getItem().getId());
        Assertions.assertEquals(booking.getItem().getName(), result.getItem().getName());
        Assertions.assertEquals(booking.getBooker().getId(), result.getBooker().getId());
        Assertions.assertEquals(booking.getBooker().getName(), result.getBooker().getName());

        verify(bookingRepository, times(1)).existsById(any());
        verify(bookingRepository, times(1)).getReferenceById(any());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetAllBookings() {
        when(bookingRepository.getAllUserBookings(1L, PageRequest.of(0,10))).
                thenReturn(new ArrayList<>());

        List<BookingOutDto> result = bookingService.getUserBookingsByState(BookingSearchState.ALL, user.getId(),
                0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).getAllUserBookings(1L,
                PageRequest.of(0,10));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetCurrentBookings() {
        when(bookingRepository.getCurrentUserBookings(1L, PageRequest.of(0,10))).
                thenReturn(new ArrayList<>());

        List<BookingOutDto> result = bookingService.getUserBookingsByState(BookingSearchState.CURRENT, user.getId(),
                0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).getCurrentUserBookings(1L,
                PageRequest.of(0,10));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetPastBookings() {
        when(bookingRepository.getPastUserBookings(1L, PageRequest.of(0,10))).
                thenReturn(new ArrayList<>());

        List<BookingOutDto> result = bookingService.getUserBookingsByState(BookingSearchState.PAST, user.getId(),
                0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).getPastUserBookings(1L,
                PageRequest.of(0,10));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetFutureBookings() {
        when(bookingRepository.getFutureUserBookings(1L, PageRequest.of(0,10))).
                thenReturn(new ArrayList<>());

        List<BookingOutDto> result = bookingService.getUserBookingsByState(BookingSearchState.FUTURE, user.getId(),
                0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).getFutureUserBookings(1L,
                PageRequest.of(0,10));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetWaitingBookings() {
        when(bookingRepository.getUserBookingsWithStatusWaiting(1L, PageRequest.of(0,10))).
                thenReturn(new ArrayList<>());

        List<BookingOutDto> result = bookingService.getUserBookingsByState(BookingSearchState.WAITING, user.getId(),
                0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).getUserBookingsWithStatusWaiting(1L,
                PageRequest.of(0,10));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetRejectedBookings() {
        when(bookingRepository.getUserBookingsWithStatusRejected(1L, PageRequest.of(0,10))).
                thenReturn(new ArrayList<>());

        List<BookingOutDto> result = bookingService.getUserBookingsByState(BookingSearchState.REJECTED, user.getId(),
                0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).getUserBookingsWithStatusRejected(1L,
                PageRequest.of(0,10));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetAllBookingsOnUserItemsByState() {
        when(bookingRepository.getAllOwnerBookings(2L, PageRequest.of(0,10))).
                thenReturn(new ArrayList<>());

        List<BookingOutDto> result = bookingService.getBookingsOnUserItemsByState(BookingSearchState.ALL, owner.getId(),
                0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).getAllOwnerBookings(2L,
                PageRequest.of(0,10));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetCurrentBookingsOnUserItemsByState() {
        when(bookingRepository.getCurrentOwnerBookings(2L, PageRequest.of(0,10))).
                thenReturn(new ArrayList<>());

        List<BookingOutDto> result = bookingService.getBookingsOnUserItemsByState(BookingSearchState.CURRENT,
                owner.getId(), 0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).getCurrentOwnerBookings(2L,
                PageRequest.of(0,10));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetPastBookingsOnUserItemsByState() {
        when(bookingRepository.getPastOwnerBookings(2L, PageRequest.of(0,10))).
                thenReturn(new ArrayList<>());

        List<BookingOutDto> result = bookingService.getBookingsOnUserItemsByState(BookingSearchState.PAST,
                owner.getId(), 0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).getPastOwnerBookings(2L,
                PageRequest.of(0,10));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetFutureBookingsOnUserItemsByState() {
        when(bookingRepository.getFutureOwnerBookings(2L, PageRequest.of(0,10))).
                thenReturn(new ArrayList<>());

        List<BookingOutDto> result = bookingService.getBookingsOnUserItemsByState(BookingSearchState.FUTURE,
                owner.getId(), 0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).getFutureOwnerBookings(2L,
                PageRequest.of(0,10));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetWaitingBookingsOnUserItemsByState() {
        when(bookingRepository.getOwnerBookingsWithStatusWaiting(2L, PageRequest.of(0,10))).
                thenReturn(new ArrayList<>());

        List<BookingOutDto> result = bookingService.getBookingsOnUserItemsByState(BookingSearchState.WAITING,
                owner.getId(), 0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).getOwnerBookingsWithStatusWaiting(2L,
                PageRequest.of(0,10));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetRejectedBookingsOnUserItemsByState() {
        when(bookingRepository.getOwnerBookingsWithStatusRejected(2L, PageRequest.of(0,10))).
                thenReturn(new ArrayList<>());

        List<BookingOutDto> result = bookingService.getBookingsOnUserItemsByState(BookingSearchState.REJECTED,
                owner.getId(), 0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).getOwnerBookingsWithStatusRejected(2L,
                PageRequest.of(0,10));
        verifyNoMoreInteractions(bookingRepository);
    }
}