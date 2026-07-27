package com.lab.reservation.controller;

import com.lab.reservation.common.Result;
import com.lab.reservation.dto.CurrentUser;
import com.lab.reservation.dto.PageResult;
import com.lab.reservation.dto.ReservationRequest;
import com.lab.reservation.dto.ReservationResponse;
import com.lab.reservation.entity.Reservation;
import com.lab.reservation.service.ReservationService;
import com.lab.reservation.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        LocalDate reservationDate = null;

        if (date != null && !date.isBlank()) {
            reservationDate = LocalDate.parse(date);
        }

        CurrentUser currentUser = AuthUtil.getCurrentUser(request);

        return Result.success(reservationService.search(deviceId, reservationDate, page, size, currentUser));
    }

    @Operation(summary = "Get reservation by id")
    @GetMapping("/{id}")
    public Result<ReservationResponse> findById(
            @PathVariable Integer id,
            HttpServletRequest request
    ) {
        CurrentUser currentUser = AuthUtil.getCurrentUser(request);

        return Result.success(reservationService.findById(id, currentUser));
    }

    @Operation(summary = "Create reservation")
    @PostMapping
    public Result<Reservation> add(
            @Valid @RequestBody ReservationRequest requestBody,
            HttpServletRequest request
    ) {
        CurrentUser currentUser = AuthUtil.getCurrentUser(request);

        return Result.success(reservationService.add(requestBody, currentUser));
    }

    @Operation(summary = "Delete reservation")
    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @PathVariable Integer id,
            HttpServletRequest request
    ) {
        CurrentUser currentUser = AuthUtil.getCurrentUser(request);

        reservationService.deleteById(id, currentUser);

        return Result.success();
    }
}