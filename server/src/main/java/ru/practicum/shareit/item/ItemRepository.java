package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i where i.owner.id = ?1")
    List<Item> findAllByOwnerId(Long userId, Pageable pageable);

    @Query("select i from Item i " +
            "where upper(i.name) like upper(concat('%', :searchQuery, '%')) " +
            " or upper(i.description) like upper(concat('%', :searchQuery, '%'))")
    List<Item> searchItems(@Param("searchQuery") String searchQuery, Pageable pageable);

    @Query("select i from Item i where i.request.id =?1 order by i.id asc")
    Optional<List<Item>> findAllByRequestId(Long requestId);
}