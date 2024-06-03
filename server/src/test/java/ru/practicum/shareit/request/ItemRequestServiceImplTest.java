package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemCreatedForRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestsRepository itemRequestsRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private User user;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test user");
        user.setEmail("test@mail.nl");

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Test description");
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now().withNano(0));

        itemRequestDto = new ItemRequestDto(1L, "Test description", LocalDateTime.now()
                .withNano(0));
    }

    @Test
    void testRequestItem() {
        when(itemRequestsRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto result = itemRequestService.requestItem(user.getId(), itemRequestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(itemRequestDto.getDescription(), result.getDescription());
        Assertions.assertEquals(itemRequestDto.getCreated(), result.getCreated());

        verify(itemRequestsRepository, times(1)).save(any(ItemRequest.class));
        verifyNoMoreInteractions(itemRequestsRepository);
    }

    @Test
    void testGetAllUserRequests() {
        when(itemRequestsRepository.findAllByRequesterId(anyLong())).thenReturn(Optional.of(new ArrayList<>()));

        List<ItemRequestOutDto> result = itemRequestService.getAllUserRequests(user.getId());

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(itemRequestsRepository, times(1)).findAllByRequesterId(anyLong());
        verifyNoMoreInteractions(itemRequestsRepository);
    }

    @Test
    void testGetAllRequests() {
        when(itemRequestsRepository.findAllOtherUsersRequests(anyLong(), any()))
                .thenReturn(new ArrayList<>());

        List<ItemRequestOutDto> result = itemRequestService.getAllRequests(user.getId(), 0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(itemRequestsRepository, times(1)).findAllOtherUsersRequests(anyLong(), any());
        verifyNoMoreInteractions(itemRequestsRepository);
    }

    @Test
    void testGetRequestById() {
        when(itemRequestsRepository.getReferenceById(any()))
                .thenReturn(itemRequest);
        when(itemRequestsRepository.existsById(itemRequest.getId()))
                .thenReturn(true);

        ItemRequestOutDto result = itemRequestService.getRequestById(user.getId(), itemRequest.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), itemRequest.getId());
        Assertions.assertEquals(result.getDescription(), itemRequest.getDescription());
        Assertions.assertEquals(result.getCreated(), itemRequest.getCreated());
        verify(itemRequestsRepository, times(1)).getReferenceById(itemRequest.getId());
        verify(itemRequestsRepository, times(1)).existsById(itemRequest.getId());
        verifyNoMoreInteractions(itemRequestsRepository);
    }

    @Test
    void testAddItemsToRequest() {
        User owner = new User();
        owner.setId(2L);
        user.setName("Owner");
        user.setEmail("owner@mail.nl");
        Item item1 = new Item("Test item1", "Test item description", true, owner);
        item1.setRequest(itemRequest);
        Item item2 = new Item("Test item2", "Test item description2", true, owner);
        item2.setRequest(itemRequest);

        ItemCreatedForRequestDto item1ForRequest = new ItemCreatedForRequestDto(item1.getId(), item1.getName(),
                item1.getDescription(), item1.getRequest().getId(), item1.getAvailable());
        ItemCreatedForRequestDto item2ForRequest = new ItemCreatedForRequestDto(item2.getId(), item2.getName(),
                item2.getDescription(), item2.getRequest().getId(), item2.getAvailable());

        List<Item> itemsForRequest = List.of(item1, item2);
        List<ItemCreatedForRequestDto> itemsForRequestDto = List.of(item1ForRequest, item2ForRequest);

        when(itemRequestsRepository.getReferenceById(any())).thenReturn(itemRequest);
        when(itemRequestsRepository.existsById(itemRequest.getId())).thenReturn(true);
        when(itemRepository.findAllByRequestId(any())).thenReturn(Optional.of(itemsForRequest));

        ItemRequestOutDto result = itemRequestService.getRequestById(user.getId(), itemRequest.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), itemRequest.getId());
        Assertions.assertEquals(result.getDescription(), itemRequest.getDescription());
        Assertions.assertEquals(result.getCreated(), itemRequest.getCreated());
        Assertions.assertEquals(result.getItems(), itemsForRequestDto);
        verify(itemRequestsRepository, times(1)).getReferenceById(itemRequest.getId());
        verify(itemRequestsRepository, times(1)).existsById(itemRequest.getId());
        verifyNoMoreInteractions(itemRequestsRepository);
        verify(itemRepository,times(1)).findAllByRequestId(any());
        verifyNoMoreInteractions(itemRepository);
    }
}