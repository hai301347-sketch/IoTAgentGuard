package com.iotguard.service;

import com.iotguard.dto.LoginDTO;
import com.iotguard.dto.RegisterDTO;
import com.iotguard.vo.LoginVO;

public interface UserService {
    LoginVO login(LoginDTO dto);
    void register(RegisterDTO dto);
}
