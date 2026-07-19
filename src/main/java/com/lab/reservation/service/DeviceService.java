package com.lab.reservation.service;

import com.lab.reservation.entity.Device;
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
        return deviceMapper.findById(id);
    }

    public Device add(Device device) {
        deviceMapper.insert(device);
        return device;
    }

    public Device update(Integer id, Device device) {
        Device oldDevice = deviceMapper.findById(id);

        if (oldDevice == null) {
            return null;
        }

        device.setId(id);
        deviceMapper.update(device);
        return deviceMapper.findById(id);
    }

    public boolean deleteById(Integer id) {
        Device oldDevice = deviceMapper.findById(id);

        if (oldDevice == null) {
            return false;
        }

        return deviceMapper.deleteById(id) > 0;
    }
}