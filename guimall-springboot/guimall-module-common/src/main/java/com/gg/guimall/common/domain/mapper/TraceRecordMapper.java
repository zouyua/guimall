package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.TraceRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 溯源记录表 Mapper
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/15
 */
@Mapper
public interface TraceRecordMapper extends BaseMapper<TraceRecordDO> {
}
