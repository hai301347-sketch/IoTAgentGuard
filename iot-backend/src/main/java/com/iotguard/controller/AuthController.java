package com.iotguard.controller;

import com.iotguard.common.Result;
import com.iotguard.dto.LoginDTO;
import com.iotguard.dto.RegisterDTO;
import com.iotguard.service.UserService;
import com.iotguard.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO dto) {
        return Result.ok(userService.login(dto));
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.ok();
    }
}
