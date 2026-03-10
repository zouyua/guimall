package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.pms.PmsProductCreateReqVO;
import com.gg.guimall.admin.model.vo.pms.PmsProductUpdateReqVO;
import com.gg.guimall.admin.model.vo.pms.FindPmsProductPageListReqVO;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

/*
 * @author:wly
 * @url:www.gg.com
 * @date:2026/3/10
 * @description:商品管理 Service*/
public interface PmsProductService {

    /**
     * 创建商品
     */
    Response createProduct(PmsProductCreateReqVO pmsProductCreateReqVO);

    /**
     * 分页查询商品
     */
    PageResponse findProductPageList(FindPmsProductPageListReqVO findPmsProductPageListReqVO);

    /**
     * 查询商品详情
     */
    Response findProductDetail(Long id);

    /**
     * 修改商品
     */
    Response updateProduct(PmsProductUpdateReqVO pmsProductUpdateReqVO);

    /**
     * 删除商品
     */
    Response deleteProduct(Long id);

}