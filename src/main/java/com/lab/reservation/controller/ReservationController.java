package com.lab.reservation.controller;

import com.lab.reservation.common.Result;
import com.lab.reservation.dto.PageResult;
import com.lab.reservation.dto.ReservationRequest;
import com.lab.reservation.dto.ReservationResponse;
import com.lab.reservation.entity.Reservation;
import com.lab.reservation.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public Result<PageResult<ReservationResponse>> search(
            @RequestParam(required = false) Integer deviceId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return Result.success(reservationService.search(deviceId, date, page, size));
    }

    @GetMapping("/reservations/{id}")
    public Result<ReservationResponse> findById(@PathVariable Integer id) {
        return Result.success(reservationService.findById(id));
    }

    @PostMapping("/reservations")
    public Result<Reservation> add(@RequestBody ReservationRequest request) {
        return Result.success(reservationService.add(request));
    }

    @DeleteMapping("/reservations/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        reservationService.deleteById(id);
        return Result.success();
    }
}