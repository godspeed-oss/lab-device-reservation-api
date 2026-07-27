package com.lab.reservation.service;

import com.lab.reservation.dto.CurrentUser;
import com.lab.reservation.dto.PageResult;
import com.lab.reservation.dto.ReservationRequest;
import com.lab.reservation.dto.ReservationResponse;
import com.lab.reservation.entity.Device;
import com.lab.reservation.entity.Reservation;
import com.lab.reservation.exception.BusinessException;
import com.lab.reservation.mapper.ReservationMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationMapper reservationMapper;
    private final DeviceService deviceService;

    public ReservationService(ReservationMapper reservationMapper, DeviceService deviceService) {
        this.reservationMapper = reservationMapper;
        this.deviceService = deviceService;
    }

    public PageResult<ReservationResponse> search(
            Integer deviceId,
            LocalDate reservationDate,
            Integer page,
            Integer size,
            CurrentUser currentUser
    ) {
        int currentPage = page == null || page < 1 ? 1 : page;
        int pageSize = size == null || size < 1 ? 10 : size;
        int offset = (currentPage - 1) * pageSize;

        Integer queryUserId = "ADMIN".equals(currentUser.getRole())
                ? null
                : currentUser.getUserId();

        List<ReservationResponse> items = reservationMapper.search(
                deviceId,
                reservationDate,
                queryUserId,
                offset,
                pageSize
        );

        long total = reservationMapper.count(deviceId, reservationDate, queryUserId);

        return new PageResult<>(items, total, currentPage, pageSize);
    }

    public ReservationResponse findById(Integer id, CurrentUser currentUser) {
        ReservationResponse reservation = reservationMapper.findById(id);

        if (reservation == null) {
            throw new BusinessException("Reservation not found");
        }

        checkReservationOwner(reservation.getUserId(), currentUser);

        return reservation;
    }

    public Reservation add(ReservationRequest request, CurrentUser currentUser) {
        Device device = deviceService.findById(request.getDeviceId());

        if (!"Available".equals(device.getStatus())) {
            throw new BusinessException("Device is not available");
        }

        boolean conflict = hasTimeConflict(
                request.getDeviceId(),
                request.getReservationDate(),
                request.getStartTime(),
                request.getEndTime(),
                0
        );

        if (conflict) {
            throw new BusinessException("Reservation time conflict");
        }

        Reservation reservation = new Reservation();
        reservation.setDeviceId(request.getDeviceId());
        reservation.setUserId(currentUser.getUserId());
        reservation.setUserName(currentUser.getUsername());
        reservation.setReservationDate(request.getReservationDate());
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());

        reservationMapper.insert(reservation);

        return reservation;
    }

    public void deleteById(Integer id, CurrentUser currentUser) {
        Reservation reservation = reservationMapper.findEntityById(id);

        if (reservation == null) {
            throw new BusinessException("Reservation not found");
        }

        checkReservationOwner(reservation.getUserId(), currentUser);

        reservationMapper.deleteById(id);
    }

    private boolean hasTimeConflict(
            Integer deviceId,
            LocalDate reservationDate,
            java.time.LocalTime startTime,
            java.time.LocalTime endTime,
            Integer excludeReservationId
    ) {
        return reservationMapper.countTimeConflict(
                deviceId,
                reservationDate,
                startTime,
                endTime,
                excludeReservationId
        ) > 0;
    }

    private void checkReservationOwner(Integer reservationUserId, CurrentUser currentUser) {
        if ("ADMIN".equals(currentUser.getRole())) {
            return;
        }

        if (!currentUser.getUserId().equals(reservationUserId)) {
            throw new BusinessException("No permission to access this reservation");
        }
    }
}