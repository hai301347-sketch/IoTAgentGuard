package com.iotguard.service.impl;

import com.iotguard.config.JwtUtil;
import com.iotguard.dto.LoginDTO;
import com.iotguard.dto.RegisterDTO;
import com.iotguard.entity.User;
import com.iotguard.mapper.UserMapper;
import com.iotguard.service.UserService;
import com.iotguard.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public LoginVO login(LoginDTO dto) {
        User user = userMapper.findByUsername(dto.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setRole(user.getRole());
        return vo;
    }

    @Override
    public void register(RegisterDTO dto) {
        User exist = userMapper.findByUsername(dto.getUsername());
        if (exist != null) {
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRealName(dto.getRealName());
        user.setRole("USER");
        user.setStatus(1);
        userMapper.insert(user);
    }
}
