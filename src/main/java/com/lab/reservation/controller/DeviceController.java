package com.lab.reservation.controller;

import com.lab.reservation.common.Result;
import com.lab.reservation.dto.DeviceRequest;
import com.lab.reservation.dto.PageResult;
import com.lab.reservation.entity.Device;
import com.lab.reservation.service.DeviceService;
import com.lab.reservation.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Device APIs")
@RestController
@RequestMapping("/devices")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Operation(summary = "Search devices")
    @GetMapping
    public Result<PageResult<Device>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return Result.success(deviceService.search(keyword, status, page, size));
    }

    @Operation(summary = "Get device by id")
    @GetMapping("/{id}")
    public Result<Device> findById(@PathVariable Integer id) {
        return Result.success(deviceService.findById(id));
    }

    @Operation(summary = "Create device")
    @PostMapping
    public Result<Device> add(
            @Valid @RequestBody DeviceRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthUtil.requireAdmin(httpRequest);
        return Result.success(deviceService.add(request));
    }

    @Operation(summary = "Update device")
    @PutMapping("/{id}")
    public Result<Device> update(
            @PathVariable Integer id,
            @Valid @RequestBody DeviceRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthUtil.requireAdmin(httpRequest);
        return Result.success(deviceService.update(id, request));
    }

    @Operation(summary = "Delete device")
    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @PathVariable Integer id,
            HttpServletRequest httpRequest
    ) {
        AuthUtil.requireAdmin(httpRequest);
        deviceService.deleteById(id);
        return Result.success();
    }
}