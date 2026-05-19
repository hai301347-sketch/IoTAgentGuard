package com.iotguard.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Device {
    private Long id;
    private String deviceName;
    private String deviceCode;
    private String deviceType;
    private String location;
    private String status;
    private String description;
    private Date createTime;
    private Date updateTime;
}
