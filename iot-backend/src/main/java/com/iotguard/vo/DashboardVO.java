package com.iotguard.vo;

import com.iotguard.entity.AlertRecord;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DashboardVO {
    private long deviceTotal;
    private long onlineDeviceCount;
    private long todayDataCount;
    private long alertCount;
    private double temperatureAvg;
    private double humidityAvg;
    private List<AlertRecord> recentAlerts;
    private List<Map<String, Object>> hourlyData;
}
