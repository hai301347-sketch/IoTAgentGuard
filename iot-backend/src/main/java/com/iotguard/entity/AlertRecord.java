package com.iotguard.entity;

import lombok.Data;
import java.util.Date;

@Data
public class AlertRecord {
    private Long id;
    private Long deviceId;
    private String deviceCode;
    private String alertType;
    private String alertLevel;
    private String alertContent;
    private Integer status;
    private Date createTime;
    private Date handleTime;
}
