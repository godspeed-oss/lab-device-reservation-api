package com.lab.reservation.service;

import com.lab.reservation.dto.ReservationRequest;
import com.lab.reservation.dto.ReservationResponse;
import com.lab.reservation.entity.Device;
import com.lab.reservation.entity.Reservation;
import com.lab.reservation.exception.BusinessException;
import com.lab.reservation.mapper.DeviceMapper;
import com.lab.reservation.mapper.ReservationMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationMapper reservationMapper;
    private final DeviceMapper deviceMapper;

    public ReservationService(ReservationMapper reservationMapper, DeviceMapper deviceMapper) {
        this.reservationMapper = reservationMapper;
        this.deviceMapper = deviceMapper;
    }

    public List<ReservationResponse> search(Integer deviceId, LocalDate date) {
        return reservationMapper.searchWithDevice(deviceId, date);
    }

    public ReservationResponse findById(Integer id) {
        ReservationResponse reservation = reservationMapper.findByIdWithDevice(id);

        if (reservation == null) {
            throw new BusinessException("Reservation not found");
        }

        return reservation;
    }

    public Reservation add(ReservationRequest request) {
        validateReservationRequest(request);

        Device device = deviceMapper.findById(request.getDeviceId());

        if (device == null) {
            throw new BusinessException("Device not found");
        }

        if (!"Available".equals(device.getStatus())) {
            throw new BusinessException("Device is not available");
        }

        Reservation reservation = new Reservation();
        reservation.setDeviceId(request.getDeviceId());
        reservation.setUserName(request.getUserName());
        reservation.setReservationDate(request.getReservationDate());
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());

        int conflictCount = reservationMapper.countTimeConflict(reservation);

        if (conflictCount > 0) {
            throw new BusinessException("Reservation time conflict");
        }

        reservationMapper.insert(reservation);
        return reservation;
    }

    public void deleteById(Integer id) {
        Reservation oldReservation = reservationMapper.findById(id);

        if (oldReservation == null) {
            throw new BusinessException("Reservation not found");
        }

        reservationMapper.deleteById(id);
    }

    private void validateReservationRequest(ReservationRequest request) {
        if (request.getDeviceId() == null) {
            throw new BusinessException("Device id is required");
        }

        if (request.getUserName() == null || request.getUserName().isBlank()) {
            throw new BusinessException("User name is required");
        }

        if (request.getReservationDate() == null) {
            throw new BusinessException("Reservation date is required");
        }

        if (request.getStartTime() == null) {
            throw new BusinessException("Start time is required");
        }

        if (request.getEndTime() == null) {
            throw new BusinessException("End time is required");
        }

        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new BusinessException("Start time must be before end time");
        }
    }
}