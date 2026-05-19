package com.iotguard.config;

import com.iotguard.entity.User;
import com.iotguard.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void run(String... args) {
        User admin = userMapper.findByUsername("admin");
        if (admin == null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User user = new User();
            user.setUsername("admin");
            user.setPassword(encoder.encode("admin123"));
            user.setRealName("系统管理员");
            user.setRole("ADMIN");
            user.setStatus(1);
            userMapper.insert(user);
            log.info("管理员账号已初始化: admin / admin123");
        }
    }
}
