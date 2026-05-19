package com.iotguard.mapper;

import com.iotguard.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    int insert(User user);
    User findById(@Param("id") Long id);
}
