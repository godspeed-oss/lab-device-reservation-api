package com.lab.reservation.entity;

import lombok.Data;

@Data
public class Device {
    private Integer id;
    private String name;
    private String type;
    private String status;
}