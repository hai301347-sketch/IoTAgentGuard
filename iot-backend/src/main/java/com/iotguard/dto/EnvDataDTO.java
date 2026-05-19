package com.iotguard.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class EnvDataDTO {
    private String deviceCode;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private Integer light;
    private Integer smoke;
}
