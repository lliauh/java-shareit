package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i where i.owner.id = ?1 order by i.id asc")
    List<Item> findAllByOwnerIdOrderById(Long userId);
    
    @Query("select i from Item i " +
            "where upper(i.name) like upper(concat('%', :searchQuery, '%')) " +
            " or upper(i.description) like upper(concat('%', :searchQuery, '%'))")
    List <Item> searchItems(@Param("searchQuery") String searchQuery);
}
