package com.saideep.parkinglotbackend.service;

import com.saideep.parkinglotbackend.dto.FloorRequestDto;
import com.saideep.parkinglotbackend.dto.ReservationRequestDto;
import com.saideep.parkinglotbackend.dto.SlotRequestDto;
import com.saideep.parkinglotbackend.exception.ResourceNotFoundException;
import com.saideep.parkinglotbackend.model.Floor;
import com.saideep.parkinglotbackend.model.Reservation;
import com.saideep.parkinglotbackend.model.Slot;
import com.saideep.parkinglotbackend.repository.FloorRepository;
import com.saideep.parkinglotbackend.repository.ReservationRepository;
import com.saideep.parkinglotbackend.repository.SlotRepository;
import com.saideep.parkinglotbackend.dto.ReservationResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingLotService {

    // Fields are now 'final'
    private final FloorRepository floorRepository;
    private final SlotRepository slotRepository;
    private final ReservationRepository reservationRepository;

    // Use @Autowired on the constructor
    @Autowired
    public ParkingLotService(FloorRepository floorRepository, SlotRepository slotRepository, ReservationRepository reservationRepository) {
        this.floorRepository = floorRepository;
        this.slotRepository = slotRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Floor createFloor(FloorRequestDto floorRequestDto) {
        Floor floor = new Floor();
        floor.setName(floorRequestDto.getName());
        return floorRepository.save(floor);
    }

    @Transactional
    public Slot createSlot(SlotRequestDto slotRequestDto) {
        Floor floor = floorRepository.findById(slotRequestDto.getFloorId())
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + slotRequestDto.getFloorId()));
        Slot slot = new Slot();
        slot.setSlotNumber(slotRequestDto.getSlotNumber());
        slot.setVehicleType(slotRequestDto.getVehicleType());
        floor.addSlot(slot);
        return slotRepository.save(slot);
    }

    @Transactional
    public Reservation reserveSlot(ReservationRequestDto reservationDto) {
        if (!reservationDto.getStartTime().isBefore(reservationDto.getEndTime())) {
            throw new IllegalArgumentException("Reservation start time must be before end time.");
        }
        if (Duration.between(reservationDto.getStartTime(), reservationDto.getEndTime()).toHours() > 24) {
            throw new IllegalArgumentException("Reservation duration cannot exceed 24 hours.");
        }

        Slot slot = slotRepository.findById(reservationDto.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + reservationDto.getSlotId()));

        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(
                reservationDto.getSlotId(), reservationDto.getStartTime(), reservationDto.getEndTime());

        if (!conflictingReservations.isEmpty()) {
            throw new IllegalArgumentException("Slot is already booked for the selected time range.");
        }

        long durationInMinutes = Duration.between(reservationDto.getStartTime(), reservationDto.getEndTime()).toMinutes();
        long hoursToBill = (long) Math.ceil(durationInMinutes / 60.0);
        double totalCost = hoursToBill * slot.getVehicleType().getHourlyRate();

        Reservation reservation = new Reservation();
        reservation.setSlot(slot);
        reservation.setVehicleNumber(reservationDto.getVehicleNumber());
        reservation.setStartTime(reservationDto.getStartTime());
        reservation.setEndTime(reservationDto.getEndTime());
        reservation.setTotalCost(totalCost);

        return reservationRepository.save(reservation);
    }

    @Transactional(readOnly = true)
    public ReservationResponseDto getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

        ReservationResponseDto responseDto = new ReservationResponseDto();

        responseDto.setReservationId(reservation.getId());
        responseDto.setSlotId(reservation.getSlot().getId()); 
        responseDto.setSlotNumber(reservation.getSlot().getSlotNumber());
        responseDto.setVehicleNumber(reservation.getVehicleNumber());
        responseDto.setStartTime(reservation.getStartTime());
        responseDto.setEndTime(reservation.getEndTime());
        responseDto.setTotalCost(reservation.getTotalCost());

        return responseDto;
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new ResourceNotFoundException("Reservation not found with id: " + reservationId);
        }
        reservationRepository.deleteById(reservationId);
    }

    @Transactional(readOnly = true)
    public List<Slot> getAvailableSlots(LocalDateTime startTime, LocalDateTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        return slotRepository.findAvailableSlots(startTime, endTime);
    }
}