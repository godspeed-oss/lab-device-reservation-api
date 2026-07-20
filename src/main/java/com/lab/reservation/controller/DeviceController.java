package com.lab.reservation.controller;

import com.lab.reservation.common.Result;
import com.lab.reservation.dto.DeviceRequest;
import com.lab.reservation.dto.PageResult;
import com.lab.reservation.entity.Device;
import com.lab.reservation.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/devices")
    public Result<PageResult<Device>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return Result.success(deviceService.search(keyword, status, page, size));
    }

    @GetMapping("/devices/{id}")
    public Result<Device> findById(@PathVariable Integer id) {
        return Result.success(deviceService.findById(id));
    }

    @PostMapping("/devices")
    public Result<Device> add(@Valid @RequestBody DeviceRequest request) {
        return Result.success(deviceService.add(request));
    }

    @PutMapping("/devices/{id}")
    public Result<Device> update(@PathVariable Integer id, @Valid @RequestBody DeviceRequest request) {
        return Result.success(deviceService.update(id, request));
    }

    @DeleteMapping("/devices/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        deviceService.deleteById(id);
        return Result.success();
    }
}