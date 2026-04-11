package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.UmsMemberDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UmsMemberMapper extends BaseMapper<UmsMemberDO> {

    default UmsMemberDO selectByUsername(String username) {
        return selectOne(new LambdaQueryWrapper<UmsMemberDO>().eq(UmsMemberDO::getUsername, username));
    }

    default UmsMemberDO selectByPhone(String phone) {
        return selectOne(new LambdaQueryWrapper<UmsMemberDO>().eq(UmsMemberDO::getPhone, phone));
    }

    /**
     * 扣减积分（下单抵扣时使用）
     * 乐观条件：integration >= delta，防止扣成负数
     */
    @Update("UPDATE ums_member SET integration = integration - #{delta}, update_time = NOW() " +
            "WHERE id = #{memberId} AND integration >= #{delta}")
    int deductIntegration(@Param("memberId") Long memberId, @Param("delta") int delta);

    /**
     * 增加积分（确认收货/后台手动赠送时使用）
     * 同时累加 history_integration
     */
    @Update("UPDATE ums_member SET integration = integration + #{delta}, " +
            "history_integration = history_integration + #{delta}, update_time = NOW() " +
            "WHERE id = #{memberId}")
    int addIntegration(@Param("memberId") Long memberId, @Param("delta") int delta);
}
