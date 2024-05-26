package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestsRepository extends JpaRepository<ItemRequest, Long> {
    @Query("select ir from ItemRequest ir where ir.requester.id = ?1 order by ir.created desc")
    Optional<List<ItemRequest>> findAllByRequesterId(Long requesterId);

    @Query("select ir from ItemRequest ir where ir.requester.id != ?1")
    List<ItemRequest> findAllOtherUsersRequests(Long requesterId, Pageable pageable);
}