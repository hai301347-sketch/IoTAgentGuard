package com.iotguard.controller;

import com.iotguard.common.Result;
import com.iotguard.mapper.AlertRecordMapper;
import com.iotguard.mapper.DeviceMapper;
import com.iotguard.mapper.EnvDataMapper;
import com.iotguard.vo.DashboardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private EnvDataMapper envDataMapper;
    @Autowired
    private AlertRecordMapper alertRecordMapper;

    @GetMapping("/stats")
    public Result<DashboardVO> stats() {
        DashboardVO vo = new DashboardVO();
        vo.setDeviceTotal(deviceMapper.countAll(new com.iotguard.dto.PageQueryDTO()));
        vo.setOnlineDeviceCount(deviceMapper.countOnline());
        vo.setTodayDataCount(envDataMapper.countToday());
        vo.setAlertCount(alertRecordMapper.countUnresolved());
        Double tempAvg = envDataMapper.avgTemperatureToday();
        Double humAvg = envDataMapper.avgHumidityToday();
        vo.setTemperatureAvg(tempAvg != null ? tempAvg : 0);
        vo.setHumidityAvg(humAvg != null ? humAvg : 0);
        vo.setRecentAlerts(alertRecordMapper.findRecent(5));
        vo.setHourlyData(envDataMapper.hourlyStatsToday());
        return Result.ok(vo);
    }
}
