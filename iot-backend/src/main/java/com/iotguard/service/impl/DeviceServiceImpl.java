package com.iotguard.service.impl;

import com.iotguard.common.PageResult;
import com.iotguard.dto.DeviceDTO;
import com.iotguard.dto.PageQueryDTO;
import com.iotguard.entity.Device;
import com.iotguard.mapper.DeviceMapper;
import com.iotguard.service.DeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public void add(DeviceDTO dto) {
        Device device = new Device();
        BeanUtils.copyProperties(dto, device);
        device.setStatus("OFFLINE");
        deviceMapper.insert(device);
    }

    @Override
    public void update(Long id, DeviceDTO dto) {
        Device device = new Device();
        BeanUtils.copyProperties(dto, device);
        device.setId(id);
        deviceMapper.update(device);
    }

    @Override
    public void delete(Long id) {
        deviceMapper.deleteById(id);
    }

    @Override
    public Device getById(Long id) {
        return deviceMapper.findById(id);
    }

    @Override
    public PageResult<Device> findByPage(PageQueryDTO query) {
        List<Device> list = deviceMapper.findByPage(query);
        long total = deviceMapper.countAll(query);
        return PageResult.of(list, total, query.getPage(), query.getSize());
    }

    @Override
    public void updateStatus(Long id, String status) {
        deviceMapper.updateStatus(id, status);
    }
}
