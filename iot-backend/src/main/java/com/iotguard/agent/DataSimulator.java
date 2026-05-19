package com.iotguard.agent;

import com.iotguard.entity.Device;
import com.iotguard.entity.EnvData;
import com.iotguard.mapper.AlertRecordMapper;
import com.iotguard.mapper.DeviceMapper;
import com.iotguard.mapper.EnvDataMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DataSimulator {
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private EnvDataMapper envDataMapper;
    @Autowired
    private AlertRecordMapper alertRecordMapper;
    @Autowired
    private AlertDetector alertDetector;

    private final Random random = new Random();
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> task;
    @Setter
    private volatile int interval = 10;

    @PostConstruct
    public void init() {
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "data-simulator");
            t.setDaemon(true);
            return t;
        });
    }

    public void start() {
        if (task != null && !task.isDone()) {
            return;
        }
        task = scheduler.scheduleAtFixedRate(this::collectData, 0, interval, TimeUnit.SECONDS);
        log.info("数据模拟器已启动，采集间隔: {}秒", interval);
    }

    public void stop() {
        if (task != null) {
            task.cancel(false);
            task = null;
            log.info("数据模拟器已停止");
        }
    }

    public boolean isRunning() {
        return task != null && !task.isDone();
    }

    public void setInterval(int seconds) {
        this.interval = seconds;
        if (isRunning()) {
            stop();
            start();
        }
    }

    private void collectData() {
        try {
            List<Device> devices = deviceMapper.findAll();
            for (Device device : devices) {
                if ("OFFLINE".equals(device.getStatus())) {
                    deviceMapper.updateStatus(device.getId(), "ONLINE");
                }
                EnvData data = generateData(device);
                envDataMapper.insert(data);
                alertDetector.check(data, device);
            }
        } catch (Exception e) {
            log.error("数据采集异常", e);
        }
    }

    private EnvData generateData(Device device) {
        EnvData data = new EnvData();
        data.setDeviceId(device.getId());
        data.setDeviceCode(device.getDeviceCode());
        data.setTemperature(randomBigDecimal(15, 45));
        data.setHumidity(randomBigDecimal(20, 90));
        data.setLight(random.nextInt(800));
        data.setSmoke(random.nextInt(80));
        data.setCollectTime(new Date());
        return data;
    }

    private BigDecimal randomBigDecimal(int min, int max) {
        double value = min + random.nextDouble() * (max - min);
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
