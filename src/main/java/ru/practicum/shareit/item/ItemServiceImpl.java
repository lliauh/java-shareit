package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingEntity;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NoPermissionException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestsRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestsRepository itemRequestsRepository;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        userService.checkIfUserExists(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.getReferenceById(userId));
        if (itemDto.getRequestId() != null) {
            item.setRequest(itemRequestsRepository.getReferenceById(itemDto.getRequestId()));
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto editItem(Long itemId, Long userId, ItemDto itemDto) {
        userService.checkIfUserExists(userId);
        checkIfItemExists(itemId);
        checkIfUserIsOwner(itemId, userId);

        Item editedItem = ItemMapper.toItem(itemDto);
        Item currentItem = itemRepository.getReferenceById(itemId);

        if (editedItem.getName() == null) {
            editedItem.setName(currentItem.getName());
        }

        if (editedItem.getDescription() == null) {
            editedItem.setDescription(currentItem.getDescription());
        }

        if (editedItem.getAvailable() == null) {
            editedItem.setAvailable(currentItem.getAvailable());
        }

        if (editedItem.getRequest() == null) {
            editedItem.setRequest(currentItem.getRequest());
        }

        editedItem.setId(itemId);
        editedItem.setOwner(currentItem.getOwner());

        return ItemMapper.toItemDto(itemRepository.save(editedItem));
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        checkIfItemExists(itemId);

        ItemOutDto itemOutDto = addBookingsToItem(itemRepository.getReferenceById(itemId), userId);
        addCommentsToItem(itemOutDto);

        return itemOutDto;
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Long userId, Integer from, Integer size) {
        userService.checkIfUserExists(userId);

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id").ascending());
        List<Item> items = itemRepository.findAllByOwnerId(userId, pageRequest);

        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            ItemOutDto itemDto = addBookingsToItem(item, userId);
            addCommentsToItem(itemDto);
            itemDtos.add(itemDto);
        }

        return itemDtos;
    }

    @Override
    public List<ItemDto> searchItems(String searchQuery, Long userId, Integer from, Integer size) {
        userService.checkIfUserExists(userId);

        if (searchQuery.isEmpty() || searchQuery.isBlank()) {
            return new ArrayList<>();
        }

        PageRequest pageRequest = PageRequest.of(from / size, size);

        return itemRepository.searchItems(searchQuery, pageRequest).stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void checkIfItemExists(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException(String.format("Item with id=%d not found", itemId));
        }
    }

    @Override
    public CommentDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        userService.checkIfUserExists(userId);
        checkIfItemExists(itemId);
        checkIfUserHasApprovedBookingInthePast(userId, itemId);

        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(itemRepository.getReferenceById(itemId));
        comment.setAuthor(userRepository.getReferenceById(userId));
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private void checkIfUserIsOwner(Long itemId, Long userId) {
        if (!itemRepository.getReferenceById(itemId).getOwner().getId().equals(userId)) {
            throw new NoPermissionException(String.format("User id=%d does not have permission to edit item id=%d",
                    userId, itemId));
        }
    }

    private ItemOutDto addBookingsToItem(Item item, Long userId) {
        ItemOutDto itemOutDto = ItemMapper.toItemOutDto(item);

        BookingEntity lbDto = new BookingEntity();
        BookingEntity nbDto = new BookingEntity();

        if (isUserAnOwner(item.getId(), userId)) {
            Optional<Booking> lastBooking = bookingRepository.getLastBooking(item.getId());
            Optional<Booking> nextBooking = bookingRepository.getNextBooking(item.getId());

            if (lastBooking.isPresent()) {
                lbDto.setId(lastBooking.get().getId());
                lbDto.setBookerId(lastBooking.get().getBooker().getId());
                itemOutDto.setLastBooking(lbDto);
            }

            if (nextBooking.isPresent()) {
                nbDto.setId(nextBooking.get().getId());
                nbDto.setBookerId(nextBooking.get().getBooker().getId());
                itemOutDto.setNextBooking(nbDto);
            }
        }

        return itemOutDto;
    }

    private void addCommentsToItem(ItemOutDto itemOutDto) {
        Optional<List<Comment>> comments = commentRepository.findAllByItemId(itemOutDto.getId());

        if (comments.isPresent()) {
            itemOutDto.setComments(comments.get().stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList()));
        }
    }

    private boolean isUserAnOwner(Long itemId, Long userId) {
        return itemRepository.getReferenceById(itemId).getOwner().getId().equals(userId);
    }

    private void checkIfUserHasApprovedBookingInthePast(Long userId, Long itemId) {
        Optional<Booking> booking = bookingRepository.getApprovedBookingInThePastByBookerIdAndItemId(userId, itemId);

        if (booking.isEmpty()) {
            throw new ValidationException(String.format("User id=%d does not have approved booking for the " +
                    "item id=%d", userId, itemId));
        }
    }
}