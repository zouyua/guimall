package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.UserDO;

import java.time.LocalDateTime;

/*
* @author:wly
* @url:www.gg.com
* @date:2026/1/28
* @description:*/
public interface UserMapper extends BaseMapper<UserDO> {
    default UserDO findByUsername(String username) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUsername, username);
        return selectOne(wrapper);
    }

//    添加updatePasswordByUsername默认方法
    default int updatePasswordByUsername(String username,String password) {
        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
//        设置要更新的字段
        wrapper.set(UserDO::getPassword,password);
        wrapper.set(UserDO::getUpdateTime, LocalDateTime.now());
//        更新条件
        wrapper.eq(UserDO::getUsername,username);

        return update(null,wrapper);
    }
}
