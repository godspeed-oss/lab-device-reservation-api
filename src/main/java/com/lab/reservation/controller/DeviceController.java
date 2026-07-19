package com.lab.reservation.controller;

import com.lab.reservation.common.Result;
import com.lab.reservation.entity.Device;
import com.lab.reservation.service.DeviceService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/devices")
    public Result<List<Device>> findAll() {
        return Result.success(deviceService.findAll());
    }

    @GetMapping("/devices/{id}")
    public Result<Device> findById(@PathVariable Integer id) {
        Device device = deviceService.findById(id);

        if (device == null) {
            return Result.fail("Device not found");
        }

        return Result.success(device);
    }

    @PostMapping("/devices")
    public Result<Device> add(@RequestBody Device device) {
        Device savedDevice = deviceService.add(device);
        return Result.success(savedDevice);
    }

    @PutMapping("/devices/{id}")
    public Result<Device> update(@PathVariable Integer id, @RequestBody Device device) {
        Device updatedDevice = deviceService.update(id, device);

        if (updatedDevice == null) {
            return Result.fail("Device not found");
        }

        return Result.success(updatedDevice);
    }

    @DeleteMapping("/devices/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        boolean success = deviceService.deleteById(id);

        if (!success) {
            return Result.fail("Device not found");
        }

        return Result.success();
    }
}