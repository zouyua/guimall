package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.admin.service.PmsProductCategoryService;
import com.gg.guimall.common.domain.dos.PmsProductCategoryDO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.mapper.PmsProductCategoryMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/12
 * @description: 商品分类 Service 实现类
 */
@Service
@RequiredArgsConstructor
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {

    /**
     * 注入商品分类 Mapper
     */
    private final PmsProductCategoryMapper categoryMapper;

    /**
     * 注入商品 Mapper（删除分类时校验是否有关联商品）
     */
    private final PmsProductMapper pmsProductMapper;

    /**
     * 新增商品分类
     */
    @Override
    public void addCategory(PmsProductCategoryCreateReqVO reqVO) {

        // 创建 DO 对象
        PmsProductCategoryDO categoryDO = new PmsProductCategoryDO();

        // 将 VO 数据复制到 DO
        BeanUtils.copyProperties(reqVO, categoryDO);

        // 插入数据库
        categoryMapper.insert(categoryDO);
    }

    /**
     * 修改商品分类
     */
    @Override
    public void updateCategory(PmsProductCategoryUpdateReqVO reqVO) {
        // 校验分类 ID 是否有效
        if (reqVO.getId() == null || reqVO.getId() <= 0) {
            throw new BizException(ResponseCodeEnum.PRODUCT_CATEGORY_NOT_FOUND);
        }

        // 校验分类是否存在
        PmsProductCategoryDO exist = categoryMapper.selectById(reqVO.getId());
        if (exist == null) {
            throw new BizException(ResponseCodeEnum.PRODUCT_CATEGORY_NOT_FOUND);
        }

        // 创建 DO 对象
        PmsProductCategoryDO categoryDO = new PmsProductCategoryDO();

        // 复制数据
        BeanUtils.copyProperties(reqVO, categoryDO);

        // 根据ID更新
        categoryMapper.updateById(categoryDO);
    }

    /**
     * 删除商品分类
     * 校验：存在子分类或关联商品时禁止删除
     */
    @Override
    public void deleteCategory(Long id) {

        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        // 校验是否存在子分类（parentId = 当前 id）
        long childCount = categoryMapper.selectCount(
                new LambdaQueryWrapper<PmsProductCategoryDO>()
                        .eq(PmsProductCategoryDO::getParentId, id)
        );
        if (childCount > 0) {
            throw new BizException(ResponseCodeEnum.CATEGORY_HAS_CHILDREN);
        }

        // 校验是否有关联商品（pms_product.product_category_id）
        long productCount = pmsProductMapper.selectCount(
                new LambdaQueryWrapper<PmsProductDO>()
                        .eq(PmsProductDO::getProductCategoryId, id)
        );
        if (productCount > 0) {
            throw new BizException(ResponseCodeEnum.CATEGORY_HAS_PRODUCTS);
        }

        categoryMapper.deleteById(id);
    }

    /**
     * 商品分类分页查询（支持搜索），返回格式与商品/农户分页一致
     */
    @Override
    public PageResponse findCategoryPage(FindPmsProductCategoryPageReqVO reqVO) {

        Page<PmsProductCategoryDO> page =
                new Page<>(reqVO.getPageNum(), reqVO.getPageSize());

        LambdaQueryWrapper<PmsProductCategoryDO> wrapper =
                new LambdaQueryWrapper<>();
        wrapper.like(
                reqVO.getName() != null && !reqVO.getName().isEmpty(),
                PmsProductCategoryDO::getName,
                reqVO.getName()
        );
        wrapper.eq(
                reqVO.getStatus() != null,
                PmsProductCategoryDO::getStatus,
                reqVO.getStatus()
        );
        wrapper.orderByAsc(PmsProductCategoryDO::getSort);

        Page<PmsProductCategoryDO> result =
                categoryMapper.selectPage(page, wrapper);

        Page<FindPmsProductCategoryPageRspVO> rspPage = new Page<>();
        rspPage.setTotal(result.getTotal());
        rspPage.setCurrent(result.getCurrent());
        rspPage.setSize(result.getSize());
        rspPage.setRecords(result.getRecords().stream().map(item -> {
            FindPmsProductCategoryPageRspVO vo =
                    new FindPmsProductCategoryPageRspVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList()));

        return PageResponse.success(rspPage);
    }

    /**
     * 查询商品分类树结构
     */
    @Override
    public List<FindPmsProductCategoryTreeRspVO> findCategoryTree() {

        // 查询所有分类
        List<PmsProductCategoryDO> list = categoryMapper.selectList(null);

        // DO -> VO
        List<FindPmsProductCategoryTreeRspVO> voList = list.stream().map(item -> {

            FindPmsProductCategoryTreeRspVO vo =
                    new FindPmsProductCategoryTreeRspVO();

            BeanUtils.copyProperties(item, vo);

            return vo;

        }).collect(Collectors.toList());

        // 构建树结构 - 使用Map优化，避免O(n²)复杂度
        return buildCategoryTree(voList);
    }

    /**
     * 使用Map构建树结构，优化性能
     */
    private List<FindPmsProductCategoryTreeRspVO> buildCategoryTree(
            List<FindPmsProductCategoryTreeRspVO> allCategories) {

        // 创建ID到分类的映射
        java.util.Map<Long, FindPmsProductCategoryTreeRspVO> categoryMap = allCategories.stream()
                .collect(java.util.stream.Collectors.toMap(
                        FindPmsProductCategoryTreeRspVO::getId,
                        category -> category,
                        (a, b) -> a
                ));

        // 收集所有根分类（parentId为0或null）
        java.util.List<FindPmsProductCategoryTreeRspVO> roots = new java.util.ArrayList<>();

        // 为每个分类设置子分类
        for (FindPmsProductCategoryTreeRspVO category : allCategories) {
            Long parentId = category.getParentId();
            if (parentId == null || Long.valueOf(0).equals(parentId)) {
                roots.add(category);
            } else {
                FindPmsProductCategoryTreeRspVO parent = categoryMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new java.util.ArrayList<>());
                    }
                    parent.getChildren().add(category);
                }
            }
        }

        return roots;
    }

    /**
     * 获取商品分类下拉列表
     * 返回扁平化的分类列表，按层级排序，便于前端下拉选择
     */
    @Override
    public List<PmsProductCategoryOptionVO> findCategoryOptions() {

        // 查询所有启用状态的分类，按sort排序
        List<PmsProductCategoryDO> list = categoryMapper.selectList(
                new LambdaQueryWrapper<PmsProductCategoryDO>()
                        .eq(PmsProductCategoryDO::getStatus, 1)
                        .orderByAsc(PmsProductCategoryDO::getSort)
        );

        // 转换为VO并构建层级结构
        return buildFlatCategoryList(list);
    }

    /**
     * 构建扁平化的分类列表（带层级信息）
     * 父分类在前，子分类在后，便于下拉框显示
     */
    private List<PmsProductCategoryOptionVO> buildFlatCategoryList(
            List<PmsProductCategoryDO> allCategories) {

        List<PmsProductCategoryOptionVO> result = new java.util.ArrayList<>();

        // 创建ID到分类的映射
        java.util.Map<Long, PmsProductCategoryDO> categoryMap = allCategories.stream()
                .collect(java.util.stream.Collectors.toMap(
                        PmsProductCategoryDO::getId,
                        category -> category,
                        (a, b) -> a
                ));

        // 找出所有根分类（parentId为0或null）
        List<PmsProductCategoryDO> roots = allCategories.stream()
                .filter(c -> c.getParentId() == null || Long.valueOf(0).equals(c.getParentId()))
                .collect(Collectors.toList());

        // 递归添加分类及其子分类
        for (PmsProductCategoryDO root : roots) {
            addCategoryWithChildren(root, categoryMap, result, 0);
        }

        return result;
    }

    /**
     * 递归添加分类及其子分类到结果列表
     */
    private void addCategoryWithChildren(
            PmsProductCategoryDO category,
            java.util.Map<Long, PmsProductCategoryDO> categoryMap,
            List<PmsProductCategoryOptionVO> result,
            int level) {

        // 添加当前分类
        PmsProductCategoryOptionVO option = PmsProductCategoryOptionVO.builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(category.getParentId())
                .level(level)
                .build();
        result.add(option);

        // 查找并添加子分类
        List<PmsProductCategoryDO> children = categoryMap.values().stream()
                .filter(c -> category.getId().equals(c.getParentId()))
                .sorted(java.util.Comparator.comparing(PmsProductCategoryDO::getSort))
                .collect(Collectors.toList());

        for (PmsProductCategoryDO child : children) {
            addCategoryWithChildren(child, categoryMap, result, level + 1);
        }
    }
}