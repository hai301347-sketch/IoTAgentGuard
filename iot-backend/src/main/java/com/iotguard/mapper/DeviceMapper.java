package com.iotguard.mapper;

import com.iotguard.entity.Device;
import com.iotguard.dto.PageQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DeviceMapper {
    int insert(Device device);
    int update(Device device);
    Device findById(@Param("id") Long id);
    Device findByCode(@Param("deviceCode") String deviceCode);
    int deleteById(@Param("id") Long id);
    List<Device> findByPage(PageQueryDTO query);
    long countAll(PageQueryDTO query);
    List<Device> findAll();
    long countOnline();
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
