package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.admin.service.PmsProductAttributeService;
import com.gg.guimall.common.domain.dos.PmsProductAttributeCategoryDO;
import com.gg.guimall.common.domain.dos.PmsProductAttributeDO;
import com.gg.guimall.common.domain.dos.PmsProductAttributeValueDO;
import com.gg.guimall.common.domain.mapper.PmsProductAttributeCategoryMapper;
import com.gg.guimall.common.domain.mapper.PmsProductAttributeMapper;
import com.gg.guimall.common.domain.mapper.PmsProductAttributeValueMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/13
 * @description: 商品属性 Service 实现类
 **/
@Service
@Slf4j
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {

    @Autowired
    private PmsProductAttributeMapper attributeMapper;

    @Autowired
    private PmsProductAttributeCategoryMapper attributeCategoryMapper;

    @Autowired
    private PmsProductAttributeValueMapper attributeValueMapper;

    /**
     * 创建商品属性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response createAttribute(PmsProductAttributeCreateReqVO reqVO) {

        // 校验属性分类是否存在
        PmsProductAttributeCategoryDO categoryDO = attributeCategoryMapper.selectById(reqVO.getProductAttributeCategoryId());
        if (Objects.isNull(categoryDO)) {
            throw new BizException(ResponseCodeEnum.ATTRIBUTE_CATEGORY_NOT_FOUND);
        }

        // 检查属性名称是否已存在
        PmsProductAttributeDO existAttribute = attributeMapper.selectOne(
                new LambdaQueryWrapper<PmsProductAttributeDO>()
                        .eq(PmsProductAttributeDO::getProductAttributeCategoryId, reqVO.getProductAttributeCategoryId())
                        .eq(PmsProductAttributeDO::getName, reqVO.getName())
                        .eq(PmsProductAttributeDO::getType, reqVO.getType())
        );
        if (Objects.nonNull(existAttribute)) {
            throw new BizException(ResponseCodeEnum.ATTRIBUTE_NAME_EXISTS);
        }

        // 构建DO对象
        PmsProductAttributeDO attributeDO = PmsProductAttributeDO.builder()
                .productAttributeCategoryId(reqVO.getProductAttributeCategoryId())
                .name(reqVO.getName())
                .selectType(Objects.nonNull(reqVO.getSelectType()) ? reqVO.getSelectType() : 0)
                .inputType(Objects.nonNull(reqVO.getInputType()) ? reqVO.getInputType() : 0)
                .inputList(reqVO.getInputList())
                .sort(Objects.nonNull(reqVO.getSort()) ? reqVO.getSort() : 0)
                .filterType(Objects.nonNull(reqVO.getFilterType()) ? reqVO.getFilterType() : 0)
                .searchType(Objects.nonNull(reqVO.getSearchType()) ? reqVO.getSearchType() : 0)
                .relatedStatus(Objects.nonNull(reqVO.getRelatedStatus()) ? reqVO.getRelatedStatus() : 0)
                .handAddStatus(Objects.nonNull(reqVO.getHandAddStatus()) ? reqVO.getHandAddStatus() : 0)
                .type(reqVO.getType())
                .build();

        // 插入数据库
        attributeMapper.insert(attributeDO);

        // 更新属性分类的属性数量或参数数量
        updateAttributeCategoryCount(reqVO.getProductAttributeCategoryId(), reqVO.getType(), 1);

        return Response.success();
    }

    /**
     * 分页查询商品属性列表
     */
    @Override
    public PageResponse findAttributePageList(FindPmsProductAttributePageReqVO reqVO) {

        Page<PmsProductAttributeDO> page = new Page<>(reqVO.getCurrent(), reqVO.getSize());

        LambdaQueryWrapper<PmsProductAttributeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Objects.nonNull(reqVO.getProductAttributeCategoryId()), 
                    PmsProductAttributeDO::getProductAttributeCategoryId, reqVO.getProductAttributeCategoryId())
                .like(Objects.nonNull(reqVO.getName()), PmsProductAttributeDO::getName, reqVO.getName())
                .eq(Objects.nonNull(reqVO.getType()), PmsProductAttributeDO::getType, reqVO.getType())
                .orderByAsc(PmsProductAttributeDO::getSort)
                .orderByDesc(PmsProductAttributeDO::getId);

        Page<PmsProductAttributeDO> resultPage = attributeMapper.selectPage(page, wrapper);

        // DO -> VO
        List<FindPmsProductAttributePageRspVO> voList = resultPage.getRecords().stream()
                .map(item -> {
                    FindPmsProductAttributePageRspVO vo = new FindPmsProductAttributePageRspVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).collect(Collectors.toList());

        return PageResponse.success(resultPage, voList);
    }

    /**
     * 修改商品属性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateAttribute(PmsProductAttributeUpdateReqVO reqVO) {

        // 校验属性是否存在
        PmsProductAttributeDO existAttribute = attributeMapper.selectById(reqVO.getId());
        if (Objects.isNull(existAttribute)) {
            throw new BizException(ResponseCodeEnum.ATTRIBUTE_NOT_FOUND);
        }

        // 校验属性分类是否存在
        PmsProductAttributeCategoryDO categoryDO = attributeCategoryMapper.selectById(reqVO.getProductAttributeCategoryId());
        if (Objects.isNull(categoryDO)) {
            throw new BizException(ResponseCodeEnum.ATTRIBUTE_CATEGORY_NOT_FOUND);
        }

        // 检查属性名称是否被其他属性使用
        PmsProductAttributeDO duplicateName = attributeMapper.selectOne(
                new LambdaQueryWrapper<PmsProductAttributeDO>()
                        .eq(PmsProductAttributeDO::getProductAttributeCategoryId, reqVO.getProductAttributeCategoryId())
                        .eq(PmsProductAttributeDO::getName, reqVO.getName())
                        .eq(PmsProductAttributeDO::getType, reqVO.getType())
                        .ne(PmsProductAttributeDO::getId, reqVO.getId())
        );
        if (Objects.nonNull(duplicateName)) {
            throw new BizException(ResponseCodeEnum.ATTRIBUTE_NAME_EXISTS);
        }

        // 如果属性分类或类型发生变化，需要更新分类的计数
        if (!existAttribute.getProductAttributeCategoryId().equals(reqVO.getProductAttributeCategoryId()) 
                || !existAttribute.getType().equals(reqVO.getType())) {
            // 旧分类计数减1
            updateAttributeCategoryCount(existAttribute.getProductAttributeCategoryId(), existAttribute.getType(), -1);
            // 新分类计数加1
            updateAttributeCategoryCount(reqVO.getProductAttributeCategoryId(), reqVO.getType(), 1);
        }

        // 构建更新对象
        PmsProductAttributeDO attributeDO = PmsProductAttributeDO.builder()
                .id(reqVO.getId())
                .productAttributeCategoryId(reqVO.getProductAttributeCategoryId())
                .name(reqVO.getName())
                .selectType(reqVO.getSelectType())
                .inputType(reqVO.getInputType())
                .inputList(reqVO.getInputList())
                .sort(reqVO.getSort())
                .filterType(reqVO.getFilterType())
                .searchType(reqVO.getSearchType())
                .relatedStatus(reqVO.getRelatedStatus())
                .handAddStatus(reqVO.getHandAddStatus())
                .type(reqVO.getType())
                .build();

        attributeMapper.updateById(attributeDO);

        return Response.success();
    }

    /**
     * 删除商品属性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteAttribute(Long id) {

        // 校验属性是否存在
        PmsProductAttributeDO attributeDO = attributeMapper.selectById(id);
        if (Objects.isNull(attributeDO)) {
            throw new BizException(ResponseCodeEnum.ATTRIBUTE_NOT_FOUND);
        }

        // 检查是否有商品使用了该属性
        Long count = attributeValueMapper.selectCount(
                new LambdaQueryWrapper<PmsProductAttributeValueDO>()
                        .eq(PmsProductAttributeValueDO::getProductAttributeId, id)
        );
        if (count > 0) {
            throw new BizException(ResponseCodeEnum.ATTRIBUTE_IN_USE);
        }

        // 执行删除
        attributeMapper.deleteById(id);

        // 更新属性分类的属性数量或参数数量
        updateAttributeCategoryCount(attributeDO.getProductAttributeCategoryId(), attributeDO.getType(), -1);

        return Response.success();
    }

    /**
     * 根据分类ID查询属性列表
     */
    @Override
    public Response findAttributeListByCategoryId(Long categoryId, Integer type) {

        List<PmsProductAttributeDO> attributeList = attributeMapper.selectByCategoryId(categoryId, type);

        // DO -> VO
        List<FindPmsProductAttributePageRspVO> voList = attributeList.stream()
                .map(item -> {
                    FindPmsProductAttributePageRspVO vo = new FindPmsProductAttributePageRspVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).collect(Collectors.toList());

        return Response.success(voList);
    }

    /**
     * 更新属性分类的属性数量或参数数量
     * @param categoryId 分类ID
     * @param type 属性类型：0规格；1参数
     * @param delta 变化量：1增加；-1减少
     */
    private void updateAttributeCategoryCount(Long categoryId, Integer type, int delta) {
        PmsProductAttributeCategoryDO categoryDO = attributeCategoryMapper.selectById(categoryId);
        if (Objects.nonNull(categoryDO)) {
            if (type == 0) {
                // 规格
                categoryDO.setAttributeCount(Math.max(0, categoryDO.getAttributeCount() + delta));
            } else {
                // 参数
                categoryDO.setParamCount(Math.max(0, categoryDO.getParamCount() + delta));
            }
            attributeCategoryMapper.updateById(categoryDO);
        }
    }
}
