package com.lab.reservation.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Reservation {
    private Integer id;
    private Integer deviceId;
    private String userName;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
}