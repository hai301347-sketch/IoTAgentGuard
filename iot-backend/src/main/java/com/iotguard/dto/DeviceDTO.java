package com.iotguard.dto;

import lombok.Data;

@Data
public class DeviceDTO {
    private String deviceName;
    private String deviceCode;
    private String deviceType;
    private String location;
    private String description;
}
