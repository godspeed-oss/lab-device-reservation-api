package com.lab.reservation.controller;

import com.lab.reservation.common.Result;
import com.lab.reservation.dto.DeviceRequest;
import com.lab.reservation.entity.Device;
import com.lab.reservation.service.DeviceService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/devices")
    public Result<List<Device>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        return Result.success(deviceService.search(keyword, status));
    }

    @GetMapping("/devices/{id}")
    public Result<Device> findById(@PathVariable Integer id) {
        return Result.success(deviceService.findById(id));
    }

    @PostMapping("/devices")
    public Result<Device> add(@RequestBody DeviceRequest request) {
        return Result.success(deviceService.add(request));
    }

    @PutMapping("/devices/{id}")
    public Result<Device> update(@PathVariable Integer id, @RequestBody DeviceRequest request) {
        return Result.success(deviceService.update(id, request));
    }

    @DeleteMapping("/devices/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        deviceService.deleteById(id);
        return Result.success();
    }
}