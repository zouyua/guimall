package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.UmsMemberReceiveAddressDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UmsMemberReceiveAddressMapper extends BaseMapper<UmsMemberReceiveAddressDO> {

    default List<UmsMemberReceiveAddressDO> selectByMemberId(Long memberId) {
        return selectList(new LambdaQueryWrapper<UmsMemberReceiveAddressDO>()
                .eq(UmsMemberReceiveAddressDO::getMemberId, memberId)
                .orderByDesc(UmsMemberReceiveAddressDO::getIsDefault));
    }
}
