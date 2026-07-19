package com.lab.reservation.service;

import com.lab.reservation.dto.DeviceRequest;
import com.lab.reservation.entity.Device;
import com.lab.reservation.exception.BusinessException;
import com.lab.reservation.mapper.DeviceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    private final DeviceMapper deviceMapper;

    public DeviceService(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    public List<Device> findAll() {
        return deviceMapper.findAll();
    }

    public Device findById(Integer id) {
        Device device = deviceMapper.findById(id);

        if (device == null) {
            throw new BusinessException("Device not found");
        }

        return device;
    }

    public Device add(DeviceRequest request) {
        validateDeviceRequest(request);

        Device device = new Device();
        device.setName(request.getName());
        device.setType(request.getType());
        device.setStatus(request.getStatus());

        deviceMapper.insert(device);
        return device;
    }

    public Device update(Integer id, DeviceRequest request) {
        Device oldDevice = deviceMapper.findById(id);

        if (oldDevice == null) {
            throw new BusinessException("Device not found");
        }

        validateDeviceRequest(request);

        Device device = new Device();
        device.setId(id);
        device.setName(request.getName());
        device.setType(request.getType());
        device.setStatus(request.getStatus());

        deviceMapper.update(device);
        return deviceMapper.findById(id);
    }

    public void deleteById(Integer id) {
        Device oldDevice = deviceMapper.findById(id);

        if (oldDevice == null) {
            throw new BusinessException("Device not found");
        }

        deviceMapper.deleteById(id);
    }

    private void validateDeviceRequest(DeviceRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new BusinessException("Device name is required");
        }

        if (request.getType() == null || request.getType().isBlank()) {
            throw new BusinessException("Device type is required");
        }

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            throw new BusinessException("Device status is required");
        }

        if (!"Available".equals(request.getStatus())
                && !"Maintenance".equals(request.getStatus())
                && !"Disabled".equals(request.getStatus())) {
            throw new BusinessException("Invalid device status");
        }
    }
}