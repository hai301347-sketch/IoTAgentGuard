package com.iotguard.controller;

import com.iotguard.common.PageResult;
import com.iotguard.common.Result;
import com.iotguard.dto.PageQueryDTO;
import com.iotguard.entity.AlertRecord;
import com.iotguard.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alert")
public class AlertController {
    @Autowired
    private AlertService alertService;

    @GetMapping("/list")
    public Result<PageResult<AlertRecord>> list(PageQueryDTO query) {
        return Result.ok(alertService.findByPage(query));
    }

    @PutMapping("/handle/{id}")
    public Result<Void> handle(@PathVariable Long id) {
        alertService.handle(id);
        return Result.ok();
    }
}
