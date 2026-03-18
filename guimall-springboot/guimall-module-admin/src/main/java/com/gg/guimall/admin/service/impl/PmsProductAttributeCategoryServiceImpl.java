package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.admin.service.PmsProductAttributeCategoryService;
import com.gg.guimall.common.domain.dos.PmsProductAttributeCategoryDO;
import com.gg.guimall.common.domain.dos.PmsProductAttributeDO;
import com.gg.guimall.common.domain.mapper.PmsProductAttributeCategoryMapper;
import com.gg.guimall.common.domain.mapper.PmsProductAttributeMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/13
 * @description: 商品属性分类 Service 实现类
 **/
@Service
@Slf4j
public class PmsProductAttributeCategoryServiceImpl implements PmsProductAttributeCategoryService {

    @Autowired
    private PmsProductAttributeCategoryMapper attributeCategoryMapper;

    @Autowired
    private PmsProductAttributeMapper attributeMapper;

    /**
     * 创建属性分类
     */
    @Override
    public Response createAttributeCategory(PmsProductAttributeCategoryCreateReqVO reqVO) {

        // 检查名称是否已存在
        PmsProductAttributeCategoryDO existCategory = attributeCategoryMapper.selectOne(
                new LambdaQueryWrapper<PmsProductAttributeCategoryDO>()
                        .eq(PmsProductAttributeCategoryDO::getName, reqVO.getName())
        );
        if (Objects.nonNull(existCategory)) {
            throw new BizException(ResponseCodeEnum.ATTRIBUTE_CATEGORY_NAME_EXISTS);
        }

        // 构建DO对象
        PmsProductAttributeCategoryDO categoryDO = PmsProductAttributeCategoryDO.builder()
                .name(reqVO.getName())
                .attributeCount(0)
                .paramCount(0)
                .build();

        // 插入数据库
        attributeCategoryMapper.insert(categoryDO);

        return Response.success();
    }

    /**
     * 分页查询属性分类列表
     */
    @Override
    public PageResponse findAttributeCategoryPageList(FindPmsProductAttributeCategoryPageReqVO reqVO) {

        Page<PmsProductAttributeCategoryDO> page = new Page<>(reqVO.getCurrent(), reqVO.getSize());

        LambdaQueryWrapper<PmsProductAttributeCategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Objects.nonNull(reqVO.getName()), PmsProductAttributeCategoryDO::getName, reqVO.getName())
                .orderByDesc(PmsProductAttributeCategoryDO::getId);

        Page<PmsProductAttributeCategoryDO> resultPage = attributeCategoryMapper.selectPage(page, wrapper);

        // DO -> VO
        List<FindPmsProductAttributeCategoryPageRspVO> voList = resultPage.getRecords().stream()
                .map(item -> {
                    FindPmsProductAttributeCategoryPageRspVO vo = new FindPmsProductAttributeCategoryPageRspVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).collect(Collectors.toList());

        return PageResponse.success(resultPage, voList);
    }

    /**
     * 修改属性分类
     */
    @Override
    public Response updateAttributeCategory(PmsProductAttributeCategoryUpdateReqVO reqVO) {

        // 校验分类是否存在
        PmsProductAttributeCategoryDO existCategory = attributeCategoryMapper.selectById(reqVO.getId());
        if (Objects.isNull(existCategory)) {
            throw new BizException(ResponseCodeEnum.ATTRIBUTE_CATEGORY_NOT_FOUND);
        }

        // 检查名称是否被其他分类使用
        PmsProductAttributeCategoryDO duplicateName = attributeCategoryMapper.selectOne(
                new LambdaQueryWrapper<PmsProductAttributeCategoryDO>()
                        .eq(PmsProductAttributeCategoryDO::getName, reqVO.getName())
                        .ne(PmsProductAttributeCategoryDO::getId, reqVO.getId())
        );
        if (Objects.nonNull(duplicateName)) {
            throw new BizException(ResponseCodeEnum.ATTRIBUTE_CATEGORY_NAME_EXISTS);
        }

        // 构建更新对象
        PmsProductAttributeCategoryDO categoryDO = PmsProductAttributeCategoryDO.builder()
                .id(reqVO.getId())
                .name(reqVO.getName())
                .build();

        attributeCategoryMapper.updateById(categoryDO);

        return Response.success();
    }

    /**
     * 删除属性分类
     */
    @Override
    public Response deleteAttributeCategory(Long id) {

        // 校验分类是否存在
        PmsProductAttributeCategoryDO categoryDO = attributeCategoryMapper.selectById(id);
        if (Objects.isNull(categoryDO)) {
            throw new BizException(ResponseCodeEnum.ATTRIBUTE_CATEGORY_NOT_FOUND);
        }

        // 检查是否有关联的属性
        Long count = attributeMapper.selectCount(
                new LambdaQueryWrapper<PmsProductAttributeDO>()
                        .eq(PmsProductAttributeDO::getProductAttributeCategoryId, id)
        );
        if (count > 0) {
            throw new BizException(ResponseCodeEnum.ATTRIBUTE_IN_USE);
        }

        // 执行删除
        attributeCategoryMapper.deleteById(id);

        return Response.success();
    }

    /**
     * 获取属性分类下拉列表
     */
    @Override
    public Response findAttributeCategoryOptions() {

        List<PmsProductAttributeCategoryDO> categoryList = attributeCategoryMapper.selectList(
                new LambdaQueryWrapper<PmsProductAttributeCategoryDO>()
                        .orderByAsc(PmsProductAttributeCategoryDO::getId)
        );

        // 转换为下拉选项格式
        List<PmsProductAttributeCategoryOptionVO> options = categoryList.stream()
                .map(category -> PmsProductAttributeCategoryOptionVO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build())
                .collect(Collectors.toList());

        return Response.success(options);
    }
}
