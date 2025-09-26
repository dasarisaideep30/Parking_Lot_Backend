package com.saideep.parkinglotbackend.repository;

import com.saideep.parkinglotbackend.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    @Query("SELECT s FROM Slot s WHERE s.id NOT IN " +
           "(SELECT r.slot.id FROM Reservation r WHERE r.startTime < :endTime AND r.endTime > :startTime)")
    List<Slot> findAvailableSlots(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}