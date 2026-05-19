package com.iotguard.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class EnvData {
    private Long id;
    private Long deviceId;
    private String deviceCode;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private Integer light;
    private Integer smoke;
    private Date collectTime;
}
