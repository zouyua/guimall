package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.pms.PmsProductFullReductionVO;
import com.gg.guimall.common.utils.Response;

import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/18
 * @description: 商品满减 Service
 **/
public interface PmsProductFullReductionService {

    /** 根据商品ID查询满减列表 */
    Response findFullReductionListByProductId(Long productId);

    /** 批量保存满减规则（先删后插，覆盖更新） */
    Response saveFullReductionList(Long productId, List<PmsProductFullReductionVO> fullReductionList);

    /** 删除单条满减规则 */
    Response deleteFullReduction(Long id);
}
