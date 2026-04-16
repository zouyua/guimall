package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gg.guimall.admin.model.vo.pms.PmsProductAttributeCategoryOptionVO;
import com.gg.guimall.admin.service.PmsProductAttributeCategoryService;
import com.gg.guimall.common.domain.dos.PmsProductAttributeCategoryDO;
import com.gg.guimall.common.domain.mapper.PmsProductAttributeCategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品属性分类 Service 实现
 */
@Service
@RequiredArgsConstructor
public class PmsProductAttributeCategoryServiceImpl implements PmsProductAttributeCategoryService {

    private final PmsProductAttributeCategoryMapper attributeCategoryMapper;

    @Override
    public List<PmsProductAttributeCategoryOptionVO> findAttributeCategoryOptions() {
        List<PmsProductAttributeCategoryDO> rows = attributeCategoryMapper.selectList(
                new LambdaQueryWrapper<PmsProductAttributeCategoryDO>()
                        .orderByAsc(PmsProductAttributeCategoryDO::getId)
        );

        return rows.stream()
                .map(row -> PmsProductAttributeCategoryOptionVO.builder()
                        .id(row.getId())
                        .name(row.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
