package com.iotguard.agent;

import com.iotguard.entity.AlertRecord;
import com.iotguard.entity.Device;
import com.iotguard.entity.EnvData;
import com.iotguard.mapper.AlertRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlertDetector {
    @Autowired
    private AlertRecordMapper alertRecordMapper;

    public void check(EnvData data, Device device) {
        if (data.getTemperature() != null) {
            double temp = data.getTemperature().doubleValue();
            if (temp > 40) {
                createAlert(device, "温度异常", "HIGH",
                        "设备" + device.getDeviceCode() + "温度达到" + temp + "°C，超过预警阈值");
            } else if (temp < 10) {
                createAlert(device, "温度过低", "MEDIUM",
                        "设备" + device.getDeviceCode() + "温度降至" + temp + "°C");
            }
        }
        if (data.getHumidity() != null) {
            double hum = data.getHumidity().doubleValue();
            if (hum > 80) {
                createAlert(device, "湿度过高", "MEDIUM",
                        "设备" + device.getDeviceCode() + "湿度达到" + hum + "%");
            } else if (hum < 20) {
                createAlert(device, "湿度过低", "MEDIUM",
                        "设备" + device.getDeviceCode() + "湿度降至" + hum + "%");
            }
        }
        if (data.getSmoke() != null && data.getSmoke() > 50) {
            createAlert(device, "烟雾超标", "CRITICAL",
                    "设备" + device.getDeviceCode() + "烟雾浓度达到" + data.getSmoke() + "，超过危险阈值");
        }
    }

    private void createAlert(Device device, String type, String level, String content) {
        AlertRecord alert = new AlertRecord();
        alert.setDeviceId(device.getId());
        alert.setDeviceCode(device.getDeviceCode());
        alert.setAlertType(type);
        alert.setAlertLevel(level);
        alert.setAlertContent(content);
        alert.setStatus(0);
        alertRecordMapper.insert(alert);
        log.warn("[告警] {} - {}", level, content);
    }
}
