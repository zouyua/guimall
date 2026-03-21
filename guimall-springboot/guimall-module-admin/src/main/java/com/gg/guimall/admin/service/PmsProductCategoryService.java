package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.common.utils.PageResponse;

import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/12
 * @description: 商品分类 Service 接口
 *
 * 主要负责商品分类的业务逻辑定义
 */
public interface PmsProductCategoryService {

    /**
     * 新增商品分类
     *
     * @param pmsProductCategoryCreateReqVO 创建分类请求参数
     */
    void addCategory(PmsProductCategoryCreateReqVO pmsProductCategoryCreateReqVO);

    /**
     * 修改商品分类
     *
     * @param pmsProductCategoryUpdateReqVO 修改分类请求参数
     */
    void updateCategory(PmsProductCategoryUpdateReqVO pmsProductCategoryUpdateReqVO);

    /**
     * 删除商品分类
     *
     * @param id 分类ID
     */
    void deleteCategory(Long id);

    /**
     * 商品分类分页查询
     *
     * 支持：
     * 1 分页
     * 2 分类名称模糊搜索
     * 3 显示状态筛选
     *
     * @param findPmsProductCategoryPageReqVO 查询条件
     * @return 分类分页数据（与商品、农户分页格式一致）
     */
    PageResponse findCategoryPage(FindPmsProductCategoryPageReqVO findPmsProductCategoryPageReqVO);

    /**
     * 查询商品分类树结构
     *
     * 用于前端 Tree 组件展示
     *
     * @return 分类树结构
     */
    List<FindPmsProductCategoryTreeRspVO> findCategoryTree();

    /**
     * 获取商品分类下拉列表
     *
     * 用于商品添加/编辑时选择分类
     * 返回扁平化的分类列表，按层级排序
     *
     * @return 分类下拉选项列表
     */
    List<PmsProductCategoryOptionVO> findCategoryOptions();
}