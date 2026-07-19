package com.lab.reservation.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationRequest {
    private Integer deviceId;
    private String userName;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
}