package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.UmsMemberDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UmsMemberMapper extends BaseMapper<UmsMemberDO> {

    default UmsMemberDO selectByUsername(String username) {
        return selectOne(new LambdaQueryWrapper<UmsMemberDO>().eq(UmsMemberDO::getUsername, username));
    }

    default UmsMemberDO selectByPhone(String phone) {
        return selectOne(new LambdaQueryWrapper<UmsMemberDO>().eq(UmsMemberDO::getPhone, phone));
    }
}
