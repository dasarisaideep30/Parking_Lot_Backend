package com.saideep.parkinglotbackend.controller;

import com.saideep.parkinglotbackend.dto.FloorRequestDto;
import com.saideep.parkinglotbackend.dto.ReservationRequestDto;
import com.saideep.parkinglotbackend.dto.SlotRequestDto;
import com.saideep.parkinglotbackend.model.Floor;
import com.saideep.parkinglotbackend.model.Reservation;
import com.saideep.parkinglotbackend.model.Slot;
import com.saideep.parkinglotbackend.service.ParkingLotService;
import com.saideep.parkinglotbackend.dto.ReservationResponseDto; // Make sure this import is here
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @Autowired
    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @PostMapping("/floors")
    public ResponseEntity<Floor> createFloor(@Valid @RequestBody FloorRequestDto floorRequestDto) {
        Floor newFloor = parkingLotService.createFloor(floorRequestDto);
        return new ResponseEntity<>(newFloor, HttpStatus.CREATED);
    }

    @PostMapping("/slots")
    public ResponseEntity<Slot> createSlot(@Valid @RequestBody SlotRequestDto slotRequestDto) {
        Slot newSlot = parkingLotService.createSlot(slotRequestDto);
        return new ResponseEntity<>(newSlot, HttpStatus.CREATED);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Reservation> reserveSlot(@Valid @RequestBody ReservationRequestDto reservationDto) {
        Reservation newReservation = parkingLotService.reserveSlot(reservationDto);
        return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
    }

    // This method is now updated to return the new DTO
    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable Long id) {
        ReservationResponseDto reservationDto = parkingLotService.getReservationById(id);
        return ResponseEntity.ok(reservationDto);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        parkingLotService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/availability")
    public ResponseEntity<List<Slot>> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<Slot> availableSlots = parkingLotService.getAvailableSlots(startTime, endTime);
        return ResponseEntity.ok(availableSlots);
    }
}