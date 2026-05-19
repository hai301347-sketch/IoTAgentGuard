package com.iotguard.service;

import com.iotguard.common.PageResult;
import com.iotguard.dto.EnvDataDTO;
import com.iotguard.dto.PageQueryDTO;
import com.iotguard.entity.EnvData;

import java.util.List;

public interface EnvDataService {
    void save(EnvDataDTO dto);
    PageResult<EnvData> findByPage(PageQueryDTO query);
    List<EnvData> findByDeviceId(Long deviceId, String startTime, String endTime);
    EnvData getLatestByDeviceId(Long deviceId);
}
