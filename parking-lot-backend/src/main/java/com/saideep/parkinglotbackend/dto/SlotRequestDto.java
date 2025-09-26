package com.saideep.parkinglotbackend.dto;

import com.saideep.parkinglotbackend.model.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SlotRequestDto {
    @NotNull(message = "Floor ID cannot be null")
    private Long floorId;

    @NotBlank(message = "Slot number cannot be blank")
    private String slotNumber;

    @NotNull(message = "Vehicle type cannot be null")
    private VehicleType vehicleType;

    // Getters and Setters
    public Long getFloorId() { return floorId; }
    public void setFloorId(Long floorId) { this.floorId = floorId; }
    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
}