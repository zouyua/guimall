package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.PmsProductCategoryDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类 Mapper
 * 继承 MyBatis-Plus BaseMapper
 * 自动拥有 CRUD 方法
 */
@Mapper
public interface PmsProductCategoryMapper extends BaseMapper<PmsProductCategoryDO> {

}