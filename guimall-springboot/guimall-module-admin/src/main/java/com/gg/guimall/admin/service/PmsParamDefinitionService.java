package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.pms.ParamDefinitionReqVO;
import com.gg.guimall.common.utils.Response;

/**
 * 参数定义 Service
 */
public interface PmsParamDefinitionService {

    Response listByCategoryId(Long categoryId);

    Response create(ParamDefinitionReqVO reqVO);

    Response update(ParamDefinitionReqVO reqVO);

    Response delete(Long id);
}
