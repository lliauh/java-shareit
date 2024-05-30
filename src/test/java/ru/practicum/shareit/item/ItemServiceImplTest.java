package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestsRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestsRepository itemRequestsRepository;

    private User user;
    private Item item;
    private ItemDto itemDto;
    private User booker;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test user");
        user.setEmail("test@mail.nl");

        booker = new User();
        booker.setId(2L);
        booker.setName("Booker");
        booker.setEmail("test2@mail.nl");

        item = new Item();
        item.setId(1L);
        item.setName("Test name");
        item.setDescription("Test description");
        item.setAvailable(true);
        item.setOwner(user);

        itemDto = new ItemDto();
        itemDto.setName("Test name");
        itemDto.setDescription("Test description");
        itemDto.setAvailable(true);
    }

    @Test
    void testAddItem() {
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.addItem(user.getId(), itemDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(itemDto.getName(), result.getName());
        Assertions.assertEquals(itemDto.getDescription(), result.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), result.getAvailable());

        verify(itemRepository, times(1)).save(any(Item.class));
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void testEditItem() {
        ItemDto updatedItemDto = new ItemDto(null, null, "Upd_desc", false);
        Item updatedItem = new Item("Test name", "Upd_desc", false);
        updatedItem.setId(1L);

        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);
        when(itemRepository.existsById(item.getId()))
                .thenReturn(true);

        ItemDto result = itemService.editItem(item.getId(), user.getId(), updatedItemDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(updatedItem.getName(), result.getName());
        Assertions.assertEquals(updatedItemDto.getDescription(), result.getDescription());
        Assertions.assertEquals(updatedItemDto.getAvailable(), result.getAvailable());

        verify(itemRepository, times(1)).save(any(Item.class));
        verify(itemRepository, times(2)).getReferenceById(anyLong());
        verify(itemRepository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void testGetItemById() {
        when(itemRepository.getReferenceById(any()))
                .thenReturn(item);
        when(itemRepository.existsById(item.getId()))
                .thenReturn(true);

        ItemDto result = itemService.getItemById(item.getId(), user.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), item.getId());
        Assertions.assertEquals(result.getName(), item.getName());
        Assertions.assertEquals(result.getDescription(), item.getDescription());
        Assertions.assertEquals(result.getAvailable(), item.getAvailable());
        verify(itemRepository, times(1)).getReferenceById(item.getId());
        verify(itemRepository, times(1)).existsById(item.getId());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void testGetAllItemsByOwner() {
        when(itemRepository.findAllByOwnerId(any(), any())).thenReturn(new ArrayList<>());

        List<ItemDto> result = itemService.getAllItemsByOwner(user.getId(), 0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(itemRepository, times(1)).findAllByOwnerId(any(), any());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void testSearchItems() {
        when(itemRepository.searchItems(any(), any())).thenReturn(new ArrayList<>());

        List<ItemDto> result = itemService.searchItems("search", user.getId(), 0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(itemRepository, times(1)).searchItems(any(), any());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void testCheckIfItemExists() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.checkIfItemExists(1L);
        });

        verify(itemRepository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void testAddComment() {
        CommentDto comment = new CommentDto();
        comment.setText("Test_comment");
        Comment savedComment = new Comment("Test_comment");
        savedComment.setId(1L);
        savedComment.setAuthor(user);
        savedComment.setItem(item);
        savedComment.setCreated(LocalDateTime.now());

        when(commentRepository.save(any())).thenReturn(savedComment);
        when(itemRepository.existsById(item.getId())).thenReturn(true);
        when(bookingRepository.getApprovedBookingInThePastByBookerIdAndItemId(any(), any()))
                .thenReturn(Optional.of(new Booking()));

        CommentDto result = itemService.addComment(item.getId(), user.getId(), comment);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), savedComment.getId());
        Assertions.assertEquals(result.getText(), savedComment.getText());
        Assertions.assertEquals(result.getAuthorName(), savedComment.getAuthor().getName());
        Assertions.assertEquals(result.getCreated(), savedComment.getCreated());

        verify(itemRepository, times(1)).existsById(anyLong());
        verify(itemRepository, times(1)).getReferenceById(anyLong());
        verifyNoMoreInteractions(itemRepository);

        verify(commentRepository, times(1)).save(any());
        verifyNoMoreInteractions(commentRepository);

        verify(bookingRepository, times(1))
                .getApprovedBookingInThePastByBookerIdAndItemId(any(), any());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetComments() {
        CommentDto comment = new CommentDto();
        comment.setText("Test_comment");
        Comment savedComment = new Comment("Test_comment");
        savedComment.setId(1L);
        savedComment.setAuthor(user);
        savedComment.setItem(item);
        savedComment.setCreated(LocalDateTime.now());

        when(commentRepository.findAllByItemId(any())).thenReturn(Optional.of(List.of(savedComment)));
        when(itemRepository.getReferenceById(any()))
                .thenReturn(item);
        when(itemRepository.existsById(item.getId()))
                .thenReturn(true);

        ItemOutDto result = itemService.getItemById(item.getId(), user.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), item.getId());
        Assertions.assertEquals(result.getName(), item.getName());
        Assertions.assertEquals(result.getDescription(), item.getDescription());
        Assertions.assertEquals(result.getAvailable(), item.getAvailable());
        Assertions.assertEquals(result.getComments().get(0).getText(), savedComment.getText());
        verify(itemRepository, times(1)).getReferenceById(item.getId());
        verify(itemRepository, times(1)).existsById(item.getId());
        verifyNoMoreInteractions(itemRepository);
        verify(commentRepository, times(1)).findAllByItemId(item.getId());
        verifyNoMoreInteractions(commentRepository);

    }
}