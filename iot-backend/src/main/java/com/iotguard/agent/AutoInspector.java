package com.iotguard.agent;

import com.iotguard.entity.Device;
import com.iotguard.mapper.AlertRecordMapper;
import com.iotguard.mapper.DeviceMapper;
import com.iotguard.mapper.EnvDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class AutoInspector {
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private EnvDataMapper envDataMapper;
    @Autowired
    private AlertRecordMapper alertRecordMapper;
    @Autowired
    private DataSimulator dataSimulator;

    public String inspect() {
        StringBuilder report = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        report.append("========== IoT系统自动巡检报告 ==========\n");
        report.append("巡检时间: ").append(sdf.format(new Date())).append("\n\n");

        // 1. 设备状态检查
        report.append("【设备状态检查】\n");
        List<Device> devices = deviceMapper.findAll();
        int online = 0, offline = 0;
        for (Device d : devices) {
            if ("ONLINE".equals(d.getStatus())) online++;
            else offline++;
        }
        report.append("  设备总数: ").append(devices.size()).append("\n");
        report.append("  在线设备: ").append(online).append("\n");
        report.append("  离线设备: ").append(offline).append("\n");
        if (offline > 0) {
            report.append("  ⚠ 发现离线设备，建议检查网络连接\n");
        } else {
            report.append("  ✓ 所有设备在线\n");
        }

        // 2. 数据采集状态
        report.append("\n【数据采集状态】\n");
        long todayCount = envDataMapper.countToday();
        report.append("  今日数据量: ").append(todayCount).append("\n");
        report.append("  模拟器状态: ").append(dataSimulator.isRunning() ? "运行中" : "已停止").append("\n");
        if (todayCount == 0) {
            report.append("  ⚠ 今日暂无数据，建议启动数据模拟器\n");
        }

        // 3. 告警状态
        report.append("\n【告警状态】\n");
        long unresolved = alertRecordMapper.countUnresolved();
        report.append("  未处理告警: ").append(unresolved).append("\n");
        if (unresolved > 0) {
            report.append("  ⚠ 存在未处理告警，请及时处理\n");
        } else {
            report.append("  ✓ 无未处理告警\n");
        }

        // 4. 修复建议
        report.append("\n【自动修复建议】\n");
        boolean hasIssue = false;
        if (offline > 0) {
            report.append("  1. 检查离线设备的网络连接和电源\n");
            hasIssue = true;
        }
        if (!dataSimulator.isRunning() && todayCount < 10) {
            report.append("  2. 建议启动数据模拟器以生成测试数据\n");
            hasIssue = true;
        }
        if (unresolved > 5) {
            report.append("  3. 未处理告警较多，建议批量处理\n");
            hasIssue = true;
        }
        if (!hasIssue) {
            report.append("  ✓ 系统运行正常，无需修复\n");
        }

        report.append("\n========== 巡检完成 ==========");
        log.info("自动巡检完成");
        return report.toString();
    }
}
