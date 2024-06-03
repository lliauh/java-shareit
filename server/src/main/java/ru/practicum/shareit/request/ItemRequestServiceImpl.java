package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemCreatedForRequestDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestsRepository itemRequestsRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto requestItem(Long userId, ItemRequestDto itemRequestDto) {
        userService.checkIfUserExists(userId);

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequester(userRepository.getReferenceById(userId));
        itemRequest.setCreated(LocalDateTime.now());

        return ItemRequestMapper.toItemRequestDto(itemRequestsRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestOutDto> getAllUserRequests(Long userId) {
        userService.checkIfUserExists(userId);

        Optional<List<ItemRequest>> requestsFromDb = itemRequestsRepository.findAllByRequesterId(userId);
        List<ItemRequestOutDto> requestsOut;

        if (requestsFromDb.isPresent()) {
            requestsOut = requestsFromDb.get().stream()
                    .map(ItemRequestMapper::toItemRequestOutDto)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }

        for (ItemRequestOutDto itemRequestOutDto : requestsOut) {
            addItemsToRequest(itemRequestOutDto);
        }

        return requestsOut;
    }

    @Override
    public List<ItemRequestOutDto> getAllRequests(Long userId, Integer from, Integer size) {
        userService.checkIfUserExists(userId);

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("created").descending());
        List<ItemRequest> requestsFromDb = itemRequestsRepository.findAllOtherUsersRequests(userId, pageRequest);
        List<ItemRequestOutDto> requestsOut = requestsFromDb.stream()
                .map(ItemRequestMapper::toItemRequestOutDto)
                .collect(Collectors.toList());

        for (ItemRequestOutDto itemRequestOutDto : requestsOut) {
            addItemsToRequest(itemRequestOutDto);
        }

        return requestsOut;
    }

    @Override
    public ItemRequestOutDto getRequestById(Long userId, Long requestId) {
        userService.checkIfUserExists(userId);
        checkIfRequestExists(requestId);

        ItemRequestOutDto requestOut = ItemRequestMapper.toItemRequestOutDto(
                itemRequestsRepository.getReferenceById(requestId)
        );

        addItemsToRequest(requestOut);

        return requestOut;
    }

    private void addItemsToRequest(ItemRequestOutDto request) {
        Optional<List<Item>> itemsFromDb = itemRepository.findAllByRequestId(request.getId());

        if (itemsFromDb.isPresent()) {
            List<ItemCreatedForRequestDto> items = itemsFromDb.get().stream()
                    .map(ItemMapper::toItemCreatedForRequestDto)
                    .collect(Collectors.toList());
            request.setItems(items);
        }
    }

    private void checkIfRequestExists(Long requestId) {
        if (!itemRequestsRepository.existsById(requestId)) {
            throw new NotFoundException(String.format("Request with id=%d not found", requestId));
        }
    }
}