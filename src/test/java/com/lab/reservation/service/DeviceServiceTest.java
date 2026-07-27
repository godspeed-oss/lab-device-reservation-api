package com.lab.reservation.service;

import com.lab.reservation.dto.DeviceRequest;
import com.lab.reservation.dto.PageResult;
import com.lab.reservation.entity.Device;
import com.lab.reservation.exception.BusinessException;
import com.lab.reservation.mapper.DeviceMapper;
import com.lab.reservation.mapper.ReservationMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeviceServiceTest {
    @Test
    void shouldSearchDevices() {
        DeviceMapper deviceMapper = Mockito.mock(DeviceMapper.class);
        ReservationMapper reservationMapper = Mockito.mock(ReservationMapper.class);
        DeviceService deviceService = new DeviceService(deviceMapper, reservationMapper);

        Device device = createDevice(1, "Microscope", "Optical Equipment", "Available");

        when(deviceMapper.search("Micro", "Available", 0, 10)).thenReturn(List.of(device));
        when(deviceMapper.count("Micro", "Available")).thenReturn(1L);

        PageResult<Device> result = deviceService.search("Micro", "Available", 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getItems().size());
        assertEquals("Microscope", result.getItems().get(0).getName());
    }

    @Test
    void shouldCreateDevice() {
        DeviceMapper deviceMapper = Mockito.mock(DeviceMapper.class);
        ReservationMapper reservationMapper = Mockito.mock(ReservationMapper.class);
        DeviceService deviceService = new DeviceService(deviceMapper, reservationMapper);

        DeviceRequest request = new DeviceRequest();
        request.setName("Camera");
        request.setType("Imaging Equipment");
        request.setStatus("Available");

        Device device = deviceService.add(request);

        assertNotNull(device);
        assertEquals("Camera", device.getName());
        assertEquals("Imaging Equipment", device.getType());
        assertEquals("Available", device.getStatus());

        verify(deviceMapper).insert(device);
    }

    @Test
    void shouldFindDeviceByIdAfterCreate() {
        DeviceMapper deviceMapper = Mockito.mock(DeviceMapper.class);
        ReservationMapper reservationMapper = Mockito.mock(ReservationMapper.class);
        DeviceService deviceService = new DeviceService(deviceMapper, reservationMapper);

        Device device = createDevice(1, "Microscope", "Optical Equipment", "Available");

        when(deviceMapper.findById(1)).thenReturn(device);

        Device result = deviceService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Microscope", result.getName());
    }

    @Test
    void shouldRejectDeleteWhenDeviceHasReservations() {
        DeviceMapper deviceMapper = Mockito.mock(DeviceMapper.class);
        ReservationMapper reservationMapper = Mockito.mock(ReservationMapper.class);
        DeviceService deviceService = new DeviceService(deviceMapper, reservationMapper);

        Device device = createDevice(1, "Microscope", "Optical Equipment", "Available");

        when(deviceMapper.findById(1)).thenReturn(device);
        when(reservationMapper.countByDeviceId(1)).thenReturn(2);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> deviceService.deleteById(1)
        );

        assertEquals("Device has reservations and cannot be deleted", exception.getMessage());
        verify(deviceMapper, never()).deleteById(anyInt());
    }

    private Device createDevice(Integer id, String name, String type, String status) {
        Device device = new Device();
        device.setId(id);
        device.setName(name);
        device.setType(type);
        device.setStatus(status);
        return device;
    }
}