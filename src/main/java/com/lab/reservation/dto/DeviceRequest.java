package com.lab.reservation.dto;

import lombok.Data;

@Data
public class DeviceRequest {
    private String name;
    private String type;
    private String status;
}