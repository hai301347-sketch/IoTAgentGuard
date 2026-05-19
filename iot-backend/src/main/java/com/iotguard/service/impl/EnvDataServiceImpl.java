package com.iotguard.service.impl;

import com.iotguard.common.PageResult;
import com.iotguard.dto.EnvDataDTO;
import com.iotguard.dto.PageQueryDTO;
import com.iotguard.entity.Device;
import com.iotguard.entity.EnvData;
import com.iotguard.mapper.DeviceMapper;
import com.iotguard.mapper.EnvDataMapper;
import com.iotguard.service.EnvDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EnvDataServiceImpl implements EnvDataService {
    @Autowired
    private EnvDataMapper envDataMapper;
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public void save(EnvDataDTO dto) {
        Device device = deviceMapper.findByCode(dto.getDeviceCode());
        if (device == null) {
            throw new RuntimeException("设备不存在: " + dto.getDeviceCode());
        }
        EnvData data = new EnvData();
        data.setDeviceId(device.getId());
        data.setDeviceCode(dto.getDeviceCode());
        data.setTemperature(dto.getTemperature());
        data.setHumidity(dto.getHumidity());
        data.setLight(dto.getLight());
        data.setSmoke(dto.getSmoke());
        data.setCollectTime(new Date());
        envDataMapper.insert(data);
    }

    @Override
    public PageResult<EnvData> findByPage(PageQueryDTO query) {
        List<EnvData> list = envDataMapper.findByPage(query);
        long total = envDataMapper.countAll(query);
        return PageResult.of(list, total, query.getPage(), query.getSize());
    }

    @Override
    public List<EnvData> findByDeviceId(Long deviceId, String startTime, String endTime) {
        return envDataMapper.findByDeviceId(deviceId, startTime, endTime);
    }

    @Override
    public EnvData getLatestByDeviceId(Long deviceId) {
        return envDataMapper.findLatestByDeviceId(deviceId);
    }
}
