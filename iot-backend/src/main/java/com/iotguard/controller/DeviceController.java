package com.iotguard.controller;

import com.iotguard.common.PageResult;
import com.iotguard.common.Result;
import com.iotguard.dto.DeviceDTO;
import com.iotguard.dto.PageQueryDTO;
import com.iotguard.entity.Device;
import com.iotguard.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/device")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @PostMapping("/add")
    public Result<Void> add(@RequestBody DeviceDTO dto) {
        deviceService.add(dto);
        return Result.ok();
    }

    @PutMapping("/update/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody DeviceDTO dto) {
        deviceService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        deviceService.delete(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<Device> getById(@PathVariable Long id) {
        return Result.ok(deviceService.getById(id));
    }

    @GetMapping("/list")
    public Result<PageResult<Device>> list(PageQueryDTO query) {
        return Result.ok(deviceService.findByPage(query));
    }

    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        deviceService.updateStatus(id, status);
        return Result.ok();
    }
}
