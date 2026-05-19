package com.iotguard.service;

import com.iotguard.common.PageResult;
import com.iotguard.dto.PageQueryDTO;
import com.iotguard.entity.AlertRecord;

public interface AlertService {
    PageResult<AlertRecord> findByPage(PageQueryDTO query);
    void handle(Long id);
}
