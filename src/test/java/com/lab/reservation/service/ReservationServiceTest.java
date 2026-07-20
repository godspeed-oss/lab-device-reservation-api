package com.lab.reservation.service;

import com.lab.reservation.dto.DeviceRequest;
import com.lab.reservation.dto.ReservationRequest;
import com.lab.reservation.entity.Device;
import com.lab.reservation.entity.Reservation;
import com.lab.reservation.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationServiceTest {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ReservationService reservationService;

    @Test
    void shouldCreateReservationWhenRequestIsValid() {
        Device device = createAvailableDevice("Reservation Test Device");

        ReservationRequest request = new ReservationRequest();
        request.setDeviceId(device.getId());
        request.setUserName("Alice");
        request.setReservationDate(LocalDate.of(2026, 8, 1));
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(LocalTime.of(11, 0));

        Reservation reservation = reservationService.add(request);

        assertNotNull(reservation.getId());
        assertEquals(device.getId(), reservation.getDeviceId());
        assertEquals("Alice", reservation.getUserName());
    }

    @Test
    void shouldRejectReservationWhenDeviceDoesNotExist() {
        ReservationRequest request = new ReservationRequest();
        request.setDeviceId(999999);
        request.setUserName("Bob");
        request.setReservationDate(LocalDate.of(2026, 8, 2));
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(LocalTime.of(11, 0));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reservationService.add(request)
        );

        assertEquals("Device not found", exception.getMessage());
    }

    @Test
    void shouldRejectReservationWhenTimeConflicts() {
        Device device = createAvailableDevice("Conflict Test Device");

        ReservationRequest firstRequest = new ReservationRequest();
        firstRequest.setDeviceId(device.getId());
        firstRequest.setUserName("Alice");
        firstRequest.setReservationDate(LocalDate.of(2026, 8, 3));
        firstRequest.setStartTime(LocalTime.of(9, 0));
        firstRequest.setEndTime(LocalTime.of(11, 0));

        reservationService.add(firstRequest);

        ReservationRequest secondRequest = new ReservationRequest();
        secondRequest.setDeviceId(device.getId());
        secondRequest.setUserName("Bob");
        secondRequest.setReservationDate(LocalDate.of(2026, 8, 3));
        secondRequest.setStartTime(LocalTime.of(10, 0));
        secondRequest.setEndTime(LocalTime.of(12, 0));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reservationService.add(secondRequest)
        );

        assertEquals("Reservation time conflict", exception.getMessage());
    }

    private Device createAvailableDevice(String name) {
        DeviceRequest request = new DeviceRequest();
        request.setName(name);
        request.setType("Test Equipment");
        request.setStatus("Available");
        return deviceService.add(request);
    }
}