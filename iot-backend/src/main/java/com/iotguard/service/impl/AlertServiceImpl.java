package com.iotguard.service.impl;

import com.iotguard.common.PageResult;
import com.iotguard.dto.PageQueryDTO;
import com.iotguard.entity.AlertRecord;
import com.iotguard.mapper.AlertRecordMapper;
import com.iotguard.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {
    @Autowired
    private AlertRecordMapper alertRecordMapper;

    @Override
    public PageResult<AlertRecord> findByPage(PageQueryDTO query) {
        List<AlertRecord> list = alertRecordMapper.findByPage(query);
        long total = alertRecordMapper.countAll(query);
        return PageResult.of(list, total, query.getPage(), query.getSize());
    }

    @Override
    public void handle(Long id) {
        alertRecordMapper.updateStatus(id, 1);
    }
}
