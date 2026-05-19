package com.iotguard.agent;

import com.iotguard.entity.EnvData;
import com.iotguard.mapper.EnvDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class NlpCommandParser {
    @Autowired
    private DataSimulator dataSimulator;
    @Autowired
    private EnvDataMapper envDataMapper;
    @Autowired
    private AutoInspector autoInspector;

    public String parse(String command) {
        if (command == null || command.trim().isEmpty()) {
            return "请输入指令";
        }
        command = command.trim();

        if (match(command, "开始采集|启动模拟|开始模拟|启动采集")) {
            dataSimulator.start();
            return "数据模拟器已启动";
        }
        if (match(command, "停止采集|停止模拟|关闭采集|关闭模拟")) {
            dataSimulator.stop();
            return "数据模拟器已停止";
        }

        Matcher m = Pattern.compile("(?:设置|修改|调整).*?(?:频率|间隔).*?(\\d+)\\s*(?:秒|s)").matcher(command);
        if (m.find()) {
            int interval = Integer.parseInt(m.group(1));
            if (interval < 1) interval = 1;
            if (interval > 3600) interval = 3600;
            dataSimulator.setInterval(interval);
            return "采集频率已设置为" + interval + "秒";
        }

        if (match(command, "巡检|检查|自检|系统检查")) {
            return autoInspector.inspect();
        }

        m = Pattern.compile("(?:查询|查看|获取).*?([Dd]\\d+)").matcher(command);
        if (m.find()) {
            String code = m.group(1).toUpperCase();
            EnvData data = envDataMapper.findLatestByDeviceId(null);
            return "暂不支持按设备编码查询，请使用前端页面查看";
        }

        if (match(command, "帮助|help|指令列表")) {
            return "支持的指令:\n" +
                   "1. 开始采集 / 启动模拟\n" +
                   "2. 停止采集 / 停止模拟\n" +
                   "3. 设置采集频率为N秒\n" +
                   "4. 系统巡检 / 系统检查";
        }

        return "无法识别指令: " + command + "\n输入「帮助」查看支持的指令";
    }

    private boolean match(String command, String pattern) {
        return command.matches(".*(?i)(" + pattern + ").*");
    }
}
