package com.saideep.parkinglotbackend.service;

import com.saideep.parkinglotbackend.dto.ReservationRequestDto;
import com.saideep.parkinglotbackend.exception.ResourceNotFoundException;
import com.saideep.parkinglotbackend.model.Reservation;
import com.saideep.parkinglotbackend.model.Slot;
import com.saideep.parkinglotbackend.model.VehicleType;
import com.saideep.parkinglotbackend.repository.FloorRepository;
import com.saideep.parkinglotbackend.repository.ReservationRepository;
import com.saideep.parkinglotbackend.repository.SlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingLotServiceTest {

    @Mock
    private SlotRepository slotRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private FloorRepository floorRepository; // Mock this even if not directly used in the test methods below

    @InjectMocks
    private ParkingLotService parkingLotService;

    @Test
    void testReserveSlot_Success() {
        // Arrange
        ReservationRequestDto request = new ReservationRequestDto();
        request.setSlotId(1L);
        request.setVehicleNumber("KA01AB1234");
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(3)); // 2 hour duration

        Slot mockSlot = new Slot();
        mockSlot.setId(1L);
        mockSlot.setVehicleType(VehicleType.FOUR_WHEELER); // Rate is 30.0

        when(slotRepository.findById(1L)).thenReturn(Optional.of(mockSlot));
        when(reservationRepository.findConflictingReservations(any(), any(), any())).thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Reservation result = parkingLotService.reserveSlot(request);

        // Assert
        assertNotNull(result);
        assertEquals(60.0, result.getTotalCost());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testReserveSlot_SlotAlreadyBooked() {
        // Arrange
        ReservationRequestDto request = new ReservationRequestDto();
        request.setSlotId(1L);
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(2));

        Slot mockSlot = new Slot();
        mockSlot.setId(1L);

        when(slotRepository.findById(1L)).thenReturn(Optional.of(mockSlot));
        when(reservationRepository.findConflictingReservations(any(), any(), any())).thenReturn(Collections.singletonList(new Reservation()));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parkingLotService.reserveSlot(request);
        });
        assertEquals("Slot is already booked for the selected time range.", exception.getMessage());
    }

    @Test
    void testGetReservationById_NotFound() {
        // Arrange
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            parkingLotService.getReservationById(1L);
        });
    }
}