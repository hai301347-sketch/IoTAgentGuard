package com.iotguard.service;

import com.iotguard.common.PageResult;
import com.iotguard.dto.DeviceDTO;
import com.iotguard.dto.PageQueryDTO;
import com.iotguard.entity.Device;

public interface DeviceService {
    void add(DeviceDTO dto);
    void update(Long id, DeviceDTO dto);
    void delete(Long id);
    Device getById(Long id);
    PageResult<Device> findByPage(PageQueryDTO query);
    void updateStatus(Long id, String status);
}
