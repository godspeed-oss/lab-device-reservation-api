package com.lab.reservation.service;

import com.lab.reservation.dto.DeviceRequest;
import com.lab.reservation.dto.PageResult;
import com.lab.reservation.entity.Device;
import com.lab.reservation.exception.BusinessException;
import com.lab.reservation.mapper.DeviceMapper;
import com.lab.reservation.mapper.ReservationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceService {
    private final DeviceMapper deviceMapper;
    private final ReservationMapper reservationMapper;

    public DeviceService(DeviceMapper deviceMapper, ReservationMapper reservationMapper) {
        this.deviceMapper = deviceMapper;
        this.reservationMapper = reservationMapper;
    }

    public PageResult<Device> search(String keyword, String status, Integer page, Integer size) {
        int safePage = page == null || page < 1 ? 1 : page;
        int safeSize = size == null || size < 1 ? 10 : size;
        int offset = (safePage - 1) * safeSize;

        List<Device> items = deviceMapper.search(keyword, status, offset, safeSize);
        long total = deviceMapper.count(keyword, status);

        return new PageResult<>(items, total, safePage, safeSize);
    }

    public Device findById(Integer id) {
        Device device = deviceMapper.findById(id);

        if (device == null) {
            throw new BusinessException("Device not found");
        }

        return device;
    }

    @Transactional
    public Device add(DeviceRequest request) {
        validateDeviceRequest(request);

        Device device = new Device();
        device.setName(request.getName());
        device.setType(request.getType());
        device.setStatus(request.getStatus());

        deviceMapper.insert(device);
        return device;
    }

    @Transactional
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

    @Transactional
    public void deleteById(Integer id) {
        Device oldDevice = deviceMapper.findById(id);

        if (oldDevice == null) {
            throw new BusinessException("Device not found");
        }

        long reservationCount = reservationMapper.countByDeviceId(id);

        if (reservationCount > 0) {
            throw new BusinessException("Device has reservations and cannot be deleted");
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