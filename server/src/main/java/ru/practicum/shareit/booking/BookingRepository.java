package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select * from bookings where booker_id = :userId order by end_date desc", nativeQuery = true)
    List<Booking> getAllUserBookings(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from bookings where booker_id = :userId and start_date <= current_timestamp " +
            "and end_date >= current_timestamp order by end_date desc", nativeQuery = true)
    List<Booking> getCurrentUserBookings(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from bookings where booker_id = :userId and end_date <= current_timestamp " +
            "order by end_date desc", nativeQuery = true)
    List<Booking> getPastUserBookings(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from bookings where booker_id = :userId and start_date >= current_timestamp " +
            "order by end_date desc", nativeQuery = true)
    List<Booking> getFutureUserBookings(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from bookings where booker_id = :userId and status = 'WAITING' " +
            "order by end_date desc", nativeQuery = true)
    List<Booking> getUserBookingsWithStatusWaiting(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from bookings where booker_id = :userId and status = 'REJECTED' " +
            "order by end_date desc", nativeQuery = true)
    List<Booking> getUserBookingsWithStatusRejected(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from bookings as b join items as i on b.item_id = i.id where i.owner_id = :userId " +
            "order by b.end_date desc", nativeQuery = true)
    List<Booking> getAllOwnerBookings(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from bookings as b join items as i on b.item_id = i.id where i.owner_id = :userId " +
            "and b.start_date <= current_timestamp and b.end_date >= current_timestamp " +
            "order by b.end_date desc", nativeQuery = true)
    List<Booking> getCurrentOwnerBookings(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from bookings as b join items as i on b.item_id = i.id where i.owner_id = :userId " +
            "and b.end_date <= current_timestamp order by b.end_date desc", nativeQuery = true)
    List<Booking> getPastOwnerBookings(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from bookings as b join items as i on b.item_id = i.id where i.owner_id = :userId " +
            "and b.start_date >= current_timestamp order by b.end_date desc", nativeQuery = true)
    List<Booking> getFutureOwnerBookings(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from bookings as b join items as i on b.item_id = i.id where i.owner_id = :userId " +
            "and b.status = 'WAITING' order by b.end_date desc", nativeQuery = true)
    List<Booking> getOwnerBookingsWithStatusWaiting(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from bookings as b join items as i on b.item_id = i.id where i.owner_id = :userId " +
            "and b.status = 'REJECTED' order by b.end_date desc", nativeQuery = true)
    List<Booking> getOwnerBookingsWithStatusRejected(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from bookings as b join items as i on b.item_id = i.id where b.item_id = :itemId " +
            "and b.start_date <= current_timestamp and b.status = 'APPROVED' order by b.end_date desc limit 1",
            nativeQuery = true)
    Optional<Booking> getLastBooking(@Param("itemId") Long itemId);

    @Query(value = "select * from bookings as b join items as i on b.item_id = i.id where b.item_id = :itemId " +
            "and b.start_date >= current_timestamp and b.status = 'APPROVED' order by b.start_date asc limit 1",
            nativeQuery = true)
    Optional<Booking> getNextBooking(@Param("itemId") Long itemId);

    @Query(value = "select * from bookings where booker_id = :userId and item_id = :itemId " +
            "and status = 'APPROVED' and end_date < current_timestamp limit 1", nativeQuery = true)
    Optional<Booking> getApprovedBookingInThePastByBookerIdAndItemId(@Param("userId") Long userId,
                                                                     @Param("itemId") Long itemId);
}