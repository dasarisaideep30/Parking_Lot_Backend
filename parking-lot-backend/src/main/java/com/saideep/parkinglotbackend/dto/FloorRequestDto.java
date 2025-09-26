package com.saideep.parkinglotbackend.dto;

import jakarta.validation.constraints.NotBlank;

public class FloorRequestDto {
    @NotBlank(message = "Floor name cannot be blank")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}