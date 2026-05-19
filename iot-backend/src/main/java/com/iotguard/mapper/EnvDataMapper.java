package com.iotguard.mapper;

import com.iotguard.entity.EnvData;
import com.iotguard.dto.PageQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface EnvDataMapper {
    int insert(EnvData envData);
    List<EnvData> findByDeviceId(@Param("deviceId") Long deviceId, @Param("startTime") String startTime, @Param("endTime") String endTime);
    EnvData findLatestByDeviceId(@Param("deviceId") Long deviceId);
    List<EnvData> findByPage(PageQueryDTO query);
    long countAll(PageQueryDTO query);
    long countToday();
    Double avgTemperatureToday();
    Double avgHumidityToday();
    List<Map<String, Object>> hourlyStatsToday();
}
