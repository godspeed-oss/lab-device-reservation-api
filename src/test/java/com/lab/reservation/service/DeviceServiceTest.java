package com.lab.reservation.service;

import com.lab.reservation.dto.DeviceRequest;
import com.lab.reservation.entity.Device;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeviceServiceTest {
    @Autowired
    private DeviceService deviceService;

    @Test
    void shouldSearchDevices() {
        assertNotNull(deviceService.search(null, null, 1, 10));
    }

    @Test
    void shouldCreateDevice() {
        DeviceRequest request = new DeviceRequest();
        request.setName("Test Camera");
        request.setType("Imaging Equipment");
        request.setStatus("Available");

        Device device = deviceService.add(request);

        assertNotNull(device.getId());
        assertEquals("Test Camera", device.getName());
        assertEquals("Imaging Equipment", device.getType());
        assertEquals("Available", device.getStatus());
    }

    @Test
    void shouldFindDeviceByIdAfterCreate() {
        DeviceRequest request = new DeviceRequest();
        request.setName("Test Microscope");
        request.setType("Optical Equipment");
        request.setStatus("Available");

        Device createdDevice = deviceService.add(request);
        Device foundDevice = deviceService.findById(createdDevice.getId());

        assertEquals(createdDevice.getId(), foundDevice.getId());
        assertEquals("Test Microscope", foundDevice.getName());
    }
}