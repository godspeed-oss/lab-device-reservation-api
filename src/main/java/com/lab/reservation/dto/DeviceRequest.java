package com.lab.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DeviceRequest {
    @NotBlank(message = "Device name is required")
    private String name;

    @NotBlank(message = "Device type is required")
    private String type;

    @NotBlank(message = "Device status is required")
    @Pattern(regexp = "Available|Maintenance|Disabled", message = "Device status must be Available, Maintenance, or Disabled")
    private String status;
}