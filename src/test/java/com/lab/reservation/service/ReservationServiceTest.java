package com.lab.reservation.service;

import com.lab.reservation.dto.CurrentUser;
import com.lab.reservation.dto.ReservationRequest;
import com.lab.reservation.entity.Device;
import com.lab.reservation.entity.Reservation;
import com.lab.reservation.exception.BusinessException;
import com.lab.reservation.mapper.ReservationMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {
    @Test
    void shouldAddReservationSuccessfully() {
        ReservationMapper reservationMapper = Mockito.mock(ReservationMapper.class);
        DeviceService deviceService = Mockito.mock(DeviceService.class);
        ReservationService reservationService = new ReservationService(reservationMapper, deviceService);

        ReservationRequest request = createReservationRequest();
        CurrentUser currentUser = createCurrentUser();

        Device device = createAvailableDevice();

        when(deviceService.findById(1)).thenReturn(device);
        when(reservationMapper.countTimeConflict(
                eq(1),
                eq(LocalDate.of(2026, 7, 20)),
                eq(LocalTime.of(9, 0)),
                eq(LocalTime.of(11, 0)),
                eq(0)
        )).thenReturn(0);

        Reservation reservation = reservationService.add(request, currentUser);

        assertNotNull(reservation);
        assertEquals(1, reservation.getDeviceId());
        assertEquals(2, reservation.getUserId());
        assertEquals("student", reservation.getUserName());
        assertEquals(LocalDate.of(2026, 7, 20), reservation.getReservationDate());
        assertEquals(LocalTime.of(9, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(11, 0), reservation.getEndTime());

        verify(reservationMapper).insert(reservation);
    }

    @Test
    void shouldRejectReservationWhenDeviceIsRepairing() {
        ReservationMapper reservationMapper = Mockito.mock(ReservationMapper.class);
        DeviceService deviceService = Mockito.mock(DeviceService.class);
        ReservationService reservationService = new ReservationService(reservationMapper, deviceService);

        ReservationRequest request = createReservationRequest();
        CurrentUser currentUser = createCurrentUser();

        Device device = new Device();
        device.setId(1);
        device.setName("3D Printer");
        device.setType("Manufacturing Equipment");
        device.setStatus("Maintenance");

        when(deviceService.findById(1)).thenReturn(device);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reservationService.add(request, currentUser)
        );

        assertEquals("Device is not available", exception.getMessage());
        verify(reservationMapper, never()).insert(any());
    }

    @Test
    void shouldRejectReservationWhenTimeConflicts() {
        ReservationMapper reservationMapper = Mockito.mock(ReservationMapper.class);
        DeviceService deviceService = Mockito.mock(DeviceService.class);
        ReservationService reservationService = new ReservationService(reservationMapper, deviceService);

        ReservationRequest request = createReservationRequest();
        CurrentUser currentUser = createCurrentUser();

        Device device = createAvailableDevice();

        when(deviceService.findById(1)).thenReturn(device);
        when(reservationMapper.countTimeConflict(
                eq(1),
                eq(LocalDate.of(2026, 7, 20)),
                eq(LocalTime.of(9, 0)),
                eq(LocalTime.of(11, 0)),
                eq(0)
        )).thenReturn(1);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reservationService.add(request, currentUser)
        );

        assertEquals("Reservation time conflict", exception.getMessage());
        verify(reservationMapper, never()).insert(any());
    }

    @Test
    void shouldRejectUserAccessingOthersReservation() {
        ReservationMapper reservationMapper = Mockito.mock(ReservationMapper.class);
        DeviceService deviceService = Mockito.mock(DeviceService.class);
        ReservationService reservationService = new ReservationService(reservationMapper, deviceService);

        CurrentUser currentUser = createCurrentUser();

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setDeviceId(1);
        reservation.setUserId(99);
        reservation.setUserName("other");
        reservation.setReservationDate(LocalDate.of(2026, 7, 20));
        reservation.setStartTime(LocalTime.of(9, 0));
        reservation.setEndTime(LocalTime.of(11, 0));

        when(reservationMapper.findEntityById(1)).thenReturn(reservation);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reservationService.deleteById(1, currentUser)
        );

        assertEquals("No permission to access this reservation", exception.getMessage());
        verify(reservationMapper, never()).deleteById(anyInt());
    }

    private ReservationRequest createReservationRequest() {
        ReservationRequest request = new ReservationRequest();
        request.setDeviceId(1);
        request.setUserName("ignored");
        request.setReservationDate(LocalDate.of(2026, 7, 20));
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(LocalTime.of(11, 0));
        return request;
    }

    private CurrentUser createCurrentUser() {
        return new CurrentUser(2, "student", "USER");
    }

    private Device createAvailableDevice() {
        Device device = new Device();
        device.setId(1);
        device.setName("Microscope");
        device.setType("Optical Equipment");
        device.setStatus("Available");
        return device;
    }
}