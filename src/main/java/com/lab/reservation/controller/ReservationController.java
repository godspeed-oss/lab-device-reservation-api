package com.lab.reservation.controller;

import com.lab.reservation.common.Result;
import com.lab.reservation.dto.PageResult;
import com.lab.reservation.dto.ReservationRequest;
import com.lab.reservation.dto.ReservationResponse;
import com.lab.reservation.entity.Reservation;
import com.lab.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Reservation APIs")
@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Search reservations")
    @GetMapping
    public Result<PageResult<ReservationResponse>> search(
            @RequestParam(required = false) Integer deviceId,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        LocalDate reservationDate = null;

        if (date != null && !date.isBlank()) {
            reservationDate = LocalDate.parse(date);
        }

        return Result.success(reservationService.search(deviceId, reservationDate, page, size));
    }

    @Operation(summary = "Get reservation by id")
    @GetMapping("/{id}")
    public Result<ReservationResponse> findById(@PathVariable Integer id) {
        return Result.success(reservationService.findById(id));
    }

    @Operation(summary = "Create reservation")
    @PostMapping
    public Result<Reservation> add(@Valid @RequestBody ReservationRequest request) {
        return Result.success(reservationService.add(request));
    }

    @Operation(summary = "Delete reservation")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        reservationService.deleteById(id);
        return Result.success();
    }
}