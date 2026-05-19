package com.iotguard.controller;

import com.iotguard.common.PageResult;
import com.iotguard.common.Result;
import com.iotguard.dto.EnvDataDTO;
import com.iotguard.dto.PageQueryDTO;
import com.iotguard.entity.EnvData;
import com.iotguard.service.EnvDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataController {
    @Autowired
    private EnvDataService envDataService;

    @PostMapping("/report")
    public Result<Void> report(@RequestBody EnvDataDTO dto) {
        envDataService.save(dto);
        return Result.ok();
    }

    @GetMapping("/list")
    public Result<PageResult<EnvData>> list(PageQueryDTO query) {
        return Result.ok(envDataService.findByPage(query));
    }

    @GetMapping("/device/{deviceId}")
    public Result<List<EnvData>> queryByDevice(@PathVariable Long deviceId,
                                                @RequestParam(required = false) String startTime,
                                                @RequestParam(required = false) String endTime) {
        return Result.ok(envDataService.findByDeviceId(deviceId, startTime, endTime));
    }

    @GetMapping("/latest/{deviceId}")
    public Result<EnvData> getLatest(@PathVariable Long deviceId) {
        return Result.ok(envDataService.getLatestByDeviceId(deviceId));
    }
}
