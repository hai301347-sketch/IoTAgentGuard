package com.iotguard.mapper;

import com.iotguard.entity.AlertRecord;
import com.iotguard.dto.PageQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AlertRecordMapper {
    int insert(AlertRecord alert);
    int updateStatus(@Param("id") Long id, @Param("status") int status);
    List<AlertRecord> findByPage(PageQueryDTO query);
    long countAll(PageQueryDTO query);
    long countUnresolved();
    List<AlertRecord> findRecent(@Param("limit") int limit);
}
