package com.iotguard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan("com.iotguard.mapper")
@EnableScheduling
public class IotGuardApplication {
    public static void main(String[] args) {
        SpringApplication.run(IotGuardApplication.class, args);
    }
}
